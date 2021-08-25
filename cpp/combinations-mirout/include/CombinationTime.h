#pragma once

#include <cstdint>
#include <ctime>

struct CombinationTime
{

    std::tm time_mark;

    CombinationTime(std::tm time_mark)
        : time_mark(time_mark)
    {
    }
    CombinationTime() = default;
    bool operator>(const CombinationTime & other) const;
    bool operator<(const CombinationTime & other) const;
    bool operator==(const CombinationTime & other) const;
    bool operator!=(const CombinationTime & other) const;
    bool operator>=(const CombinationTime & other) const;
    bool operator<=(const CombinationTime & other) const;
    void add_month(std::int32_t val);
    void add_quartal(std::int32_t val);
    void add_year(std::int32_t val);
    void add_day(std::int32_t val);
};