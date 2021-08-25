#pragma once
#include "CombinationTime.h"
#include "Component.h"

#include <map>
#include <utility>
#include <variant>

struct Information
{
    std::map<char, double> strike_variable;
    std::map<char, CombinationTime> expiration_variable;
    double last_fixed_strike_value;
    double last_strike_value;
    CombinationTime last_expiration_value;
    CombinationTime last_fixed_expiration_value;
    int32_t strike_offset;
    int32_t expiration_offset;

    Information() = default;
};

enum TimeEnum : char
{
    DAY = 'd',
    MONTH = 'm',
    YEAR = 'y',
    QUARTER = 'q',
};

struct TimeMark
{
    int32_t value;
    TimeEnum type;

    TimeMark(int32_t value, char type)
        : value(value)
        , type(static_cast<TimeEnum>(type))
    {
    }
};

struct Leg
{
    InstrumentType type;
    std::variant<double, char> ratio;
    std::variant<char, int32_t, TimeMark> expiration;
    std::variant<char, int32_t> strike;

    bool checkComponent(const Component & component, Information & info) const;

    Leg(char type, std::variant<double, char> ratio, std::variant<char, int32_t, TimeMark> expiration, std::variant<char, int32_t> strike)
        : type(static_cast<InstrumentType>(type))
        , ratio(ratio)
        , expiration(std::move(expiration))
        , strike(std::move(strike)){};
};