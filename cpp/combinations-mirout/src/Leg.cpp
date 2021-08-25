//
// Created by mirout on 07.06.2021.
//

#include "Leg.h"

#include "CombinationTime.h"

#include <chrono>
#include <cmath>
#include <ctime>
#include <iomanip>
#include <iostream>

#define EPS 0.00001

bool equal(double a, double b)
{
    return std::abs(a - b) < EPS;
}

bool equal(const std::tm & a, const std::tm & b)
{
    return a.tm_year == b.tm_year && a.tm_mon == b.tm_mon && a.tm_mday == b.tm_mday;
}

bool more(const std::tm & a, const std::tm & b)
{
    return a.tm_year > b.tm_year || (a.tm_year == b.tm_year && a.tm_mon > b.tm_mon) || (a.tm_year == b.tm_year && a.tm_mon == b.tm_mon && a.tm_mday > b.tm_mday);
}

bool less(const std::tm & a, const std::tm & b)
{
    return a.tm_year < b.tm_year || (a.tm_year == b.tm_year && a.tm_mon < b.tm_mon) || (a.tm_year == b.tm_year && a.tm_mon == b.tm_mon && a.tm_mday < b.tm_mday);
}

void normalize(std::tm & a)
{
    if (a.tm_mon > 11) {
        a.tm_year += (a.tm_mon / 12);
        a.tm_mon %= 12;
    }
}

//Leg::Leg(char type, double ratio, std::variant<char, std::string> expiration, std::variant<char, int32_t> strike)
//    : type(static_cast<InstrumentType>(type))
//    , ratio(ratio)
//    , expiration(expiration)
//    , strike(strike)
//    {}

bool Leg::checkComponent(const Component & component, Information & info) const
{
    if (component.type != type) {
        if (!(type == InstrumentType::O && (component.type == InstrumentType::P || component.type == InstrumentType::C))) {
            return false;
        }
    }

    // Check ratio
    if (std::holds_alternative<char>(ratio)) {
        if (std::get<char>(ratio) == '+' && component.ratio < 0) {
            return false;
        }
        if (std::get<char>(ratio) == '-' && component.ratio > 0) {
            return false;
        }
    }
    else if (!equal(std::get<double>(ratio), component.ratio)) {
        return false;
    }

    //Check strike
    if (std::holds_alternative<char>(strike)) {
        const auto [iter, insert] = info.strike_variable.try_emplace(std::get<char>(strike), component.strike);
        if (!insert && !equal(iter->second, component.strike)) {
            return false;
        }
        info.last_fixed_strike_value = component.strike;
        info.strike_offset = 0;
    }
    else if (std::get<int32_t>(strike) == 0) {
        info.last_fixed_strike_value = component.strike;
        info.strike_offset = 0;
    }
    else {
        if (info.strike_offset == std::get<int32_t>(strike)) {
            if (!equal(info.last_strike_value, component.strike)) {
                return false;
            }
        }
        else if (std::get<int32_t>(strike) > 0 && info.strike_offset >= 0) {
            if (info.last_strike_value >= component.strike) {
                return false;
            }
        }
        else if (std::get<int32_t>(strike) < 0 && info.strike_offset <= 0) {
            if (info.last_strike_value <= component.strike) {
                return false;
            }
        }
        else {
            if (std::get<int32_t>(strike) > 0 && info.last_fixed_strike_value <= component.strike) {
                return false;
            }
            if (std::get<int32_t>(strike) < 0 && info.last_fixed_strike_value >= component.strike) {
                return false;
            }
        }
        info.strike_offset = std::get<int32_t>(strike);
    }

    info.last_strike_value = component.strike;
    const CombinationTime time{component.expiration};

    //Check expiration
    if (std::holds_alternative<char>(expiration)) {
        const auto [iter, insert] = info.expiration_variable.try_emplace(std::get<char>(expiration), time);
        if (!insert && time != iter->second) {
            return false;
        }
        info.last_fixed_expiration_value = time;
        info.expiration_offset = 0;
    }
    else if (std::holds_alternative<int32_t>(expiration)) {
        int32_t offset = std::get<int32_t>(expiration);
        if (offset == 0) {
            info.last_fixed_expiration_value = time;
        }
        else if (info.expiration_offset == offset) {
            if (info.last_expiration_value != time) {
                return false;
            }
        }
        else if (info.expiration_offset >= 0 && offset > 0) {
            if (info.last_expiration_value >= time) {
                return false;
            }
        }
        else if (info.expiration_offset <= 0 && offset < 0) {
            if (info.last_expiration_value <= time) {
                return false;
            }
        }
        else {
            if (offset > 0 && info.last_fixed_expiration_value <= time) {
                return false;
            }
            else if (offset < 0 && info.last_fixed_expiration_value >= time) {
                return false;
            }
        }
        info.expiration_offset = offset;
    }
    else {
        const auto expiration_offset = std::get<TimeMark>(expiration);
        auto copy = info.last_fixed_expiration_value;
        switch (expiration_offset.type) {
        case DAY: {
            copy.add_day(expiration_offset.value);
            if (copy != time) {
                return false;
            }
            break;
        }
        case MONTH: {
            copy.add_month(expiration_offset.value);
            if (copy != time) {
                return false;
            }
            break;
        }
        case YEAR: {
            copy.add_year(expiration_offset.value);
            if (copy != time) {
                return false;
            }
            break;
        }
        case QUARTER: {
            copy.add_quartal(expiration_offset.value);
            if (copy > time) {
                return false;
            }
            copy.add_quartal(1);
            if (copy < time) {
                return false;
            }
            break;
        }
        }
    }

    info.last_expiration_value = time;

    return true;
}
