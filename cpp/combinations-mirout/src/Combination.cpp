//
// Created by mirout on 07.06.2021.
//

#include "Combination.h"

#include "Leg.h"

#include <algorithm>
#include <ctime>
#include <iostream>
#include <map>

void Combination::add_leg(Leg && l)
{
    legs.push_back(std::move(l));
}

void Combination::build_answer(const std::vector<std::pair<size_t, Component>> & components, std::vector<int> & order)
{
    order.clear();
    order.resize(components.size(), 0);
    for (size_t i = 0; i < components.size(); i++) {
        order[components[i].first] = i + 1;
    }
}

bool Combination::match(const std::vector<Component> & components, std::vector<int> & order) const
{
    std::vector<std::pair<size_t, Component>> perm;
    for (size_t i = 0; i < components.size(); i++) {
        perm.emplace_back(i, components[i]);
    }
    if (!pred_test(components.size())) {
        return false;
    }
    if (match_impl(perm)) {
        build_answer(perm, order);
        return true;
    }
    return false;
}

bool CombinationFixed::match_impl(std::vector<std::pair<size_t, Component>> & components) const
{
    do {
        bool result = true;
        Information info;
        for (size_t i = 0; i < components.size(); i++) {
            if (!legs[i].checkComponent(components[i].second, info)) {
                result = false;
                break;
            }
        }
        if (result) {
            return true;
        }
    } while (std::next_permutation(components.begin(), components.end(), [](std::pair<size_t, Component> l, std::pair<size_t, Component> r) { return l.first <= r.first; }));

    return false;
}

bool CombinationFixed::pred_test(const size_t components_size) const
{
    return size() == components_size;
}

bool CombinationMultiple::match_impl(std::vector<std::pair<size_t, Component>> & components) const
{
    const auto it = std::find_if(components.begin(), components.end(), [](std::pair<size_t, Component> comp) {
        return comp.second.type != InstrumentType::F;
    });
    if (it != components.end()) {
        return false;
    }
    do {
        bool result = true;
        Information info;
        for (size_t i = 0; i < components.size(); i++) {
            if (!legs[i % size()].checkComponent(components[i].second, info)) {
                result = false;
                break;
            }
        }
        if (result) {
            return true;
        }
    } while (std::next_permutation(components.begin(), components.end(), [](std::pair<size_t, Component> l, std::pair<size_t, Component> r) { return l.first <= r.first; }));

    return false;
}

bool CombinationMultiple::pred_test(size_t components_size) const
{
    return components_size % size() == 0 && components_size >= size();
}

bool CombinationMore::match_impl(std::vector<std::pair<size_t, Component>> & components) const
{
    bool result = true;
    Information info;
    for (size_t i = 0; i < components.size(); i++) {
        if (!legs[i % size()].checkComponent(components[i].second, info)) {
            result = false;
            break;
        }
    }
    return result;
}

bool CombinationMore::pred_test(size_t components_size) const
{
    return min_count <= components_size;
}

const std::string & Combination::get_name() const
{
    return name;
}

size_t Combination::size() const
{
    return legs.size();
}