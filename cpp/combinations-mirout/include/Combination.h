//
// Created by mirout on 07.06.2021.
//

#pragma once
#include "Leg.h"

#include <set>
#include <utility>
#include <vector>

class Combination
{
public:
    size_t size() const;
    bool match(const std::vector<Component> & components, std::vector<int> & order) const;
    const std::string & get_name() const;

    void add_leg(Leg && l);

    Combination(std::string name)
        : name(std::move(name)){};
    virtual ~Combination(){};

private:
    static void build_answer(const std::vector<std::pair<size_t, Component>> & components, std::vector<int> & order);
    virtual bool match_impl(std::vector<std::pair<size_t, Component>> &) const
    {
        return false;
    };

    virtual bool pred_test(size_t) const
    {
        return false;
    };

    std::string name;

protected:
    std::vector<Leg> legs{};
};

class CombinationFixed : public Combination
{
public:
    CombinationFixed(std::string name)
        : Combination(std::move(name)){};
    ~CombinationFixed() {}

private:
    bool match_impl(std::vector<std::pair<size_t, Component>> & components) const override;
    bool pred_test(size_t components_size) const override;
};

class CombinationMultiple : public Combination
{
public:
    CombinationMultiple(std::string name)
        : Combination(std::move(name)){};
    ~CombinationMultiple() {}

private:
    bool match_impl(std::vector<std::pair<size_t, Component>> & components) const override;
    bool pred_test(size_t components_size) const override;
};

class CombinationMore : public Combination
{
public:
    CombinationMore(std::string name, size_t min_count)
        : Combination(std::move(name))
        , min_count(min_count){};
    ~CombinationMore() {}

private:
    size_t min_count;
    bool match_impl(std::vector<std::pair<size_t, Component>> & components) const override;
    bool pred_test(size_t components_size) const override;
};