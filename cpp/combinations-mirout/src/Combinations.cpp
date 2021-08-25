#include "Combinations.h"

#include "Combination.h"
#include "Component.h"
#include "Leg.h"
#include "pugixml.hpp"

#include <cstring>
#include <iostream>
#include <memory>
#include <string>

bool Combinations::load(const std::filesystem::path & resource)
{
    pugi::xml_document doc;

    auto result = doc.load_file(resource.string().c_str());

    if (!result || !doc.child("combinations")) {
        return false;
    }

    for (const auto & elem : doc.child("combinations").children()) {

        const std::string name = elem.attribute("name").as_string();

        auto legs = elem.child("legs");
        auto cardinality = legs.attribute("cardinality").as_string();
        size_t min_count = 0;

        if (strcmp(cardinality, "fixed") == 0) {
            combinations.push_back(std::make_unique<CombinationFixed>(name));
        }
        else if (strcmp(cardinality, "more") == 0) {
            min_count = legs.attribute("mincount").as_uint();
            combinations.push_back(std::make_unique<CombinationMore>(name, min_count));
        }
        else {
            combinations.push_back(std::make_unique<CombinationMultiple>(name));
        }

        auto & combination = combinations.back();

        std::vector<Leg> vector_legs{};

        for (const auto & leg : legs.children()) {

            const char type_leg = leg.attribute("type").as_string()[0];

            std::variant<double, char> ratio;

            // Ratio parse
            if (leg.attribute("ratio").as_double() == 0) {
                ratio = leg.attribute("ratio").as_string()[0];
            }
            else {
                ratio = leg.attribute("ratio").as_double();
            }

            std::variant<char, int32_t> strike = 0;

            // Stike parse
            const auto strike_attribute = leg.attribute("strike");
            const auto strike_offset_attribute = leg.attribute("strike_offset");

            if (strike_attribute) {
                strike = strike_attribute.as_string()[0];
            }
            else if (strike_offset_attribute) {
                auto str = strike_offset_attribute.as_string();
                strike = static_cast<int32_t>(strlen(str));
                if (str[0] == '-') {
                    strike = -std::get<int32_t>(strike);
                }
            }

            std::variant<char, int32_t, TimeMark> expiration = 0;

            // Expiration parse
            const auto expiration_attribute = leg.attribute("expiration");
            const auto expiration_offset_attribute = leg.attribute("expiration_offset");

            if (expiration_attribute) {
                expiration = expiration_attribute.as_string()[0];
            }
            else if (expiration_offset_attribute) {
                const auto offset = expiration_offset_attribute.as_string();
                const size_t string_len = strlen(offset);

                if (offset[0] == '+' || offset[0] == '-') {
                    expiration = static_cast<int32_t>(string_len);
                    if (offset[0] == '-') {
                        expiration = -std::get<int32_t>(expiration);
                    }
                }
                else {
                    int32_t number = atoi(offset);
                    if (number == 0) {
                        number = 1;
                    }
                    expiration = TimeMark{number, offset[string_len - 1]};
                }
            }
            combination->add_leg(Leg{type_leg, ratio, expiration, strike});
        }
    }
    return true;
}

std::string Combinations::classify(const std::vector<Component> & components, std::vector<int> & order) const
{
    for (const auto & comb : combinations) {
        if (comb->match(components, order)) {
            return comb->get_name();
        }
    }
    order.clear();
    return "Unclassified";
}
