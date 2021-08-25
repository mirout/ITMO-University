#include "CombinationTime.h"

bool CombinationTime::operator<(const CombinationTime & other) const
{
    return time_mark.tm_year < other.time_mark.tm_year ||
            (time_mark.tm_year == other.time_mark.tm_year && time_mark.tm_mon < other.time_mark.tm_mon) ||
            (time_mark.tm_year == other.time_mark.tm_year && time_mark.tm_mon == other.time_mark.tm_mon && time_mark.tm_mday < other.time_mark.tm_mday);
}
bool CombinationTime::operator>(const CombinationTime & other) const
{
    return time_mark.tm_year > other.time_mark.tm_year ||
            (time_mark.tm_year == other.time_mark.tm_year && time_mark.tm_mon > other.time_mark.tm_mon) ||
            (time_mark.tm_year == other.time_mark.tm_year && time_mark.tm_mon == other.time_mark.tm_mon && time_mark.tm_mday > other.time_mark.tm_mday);
}
bool CombinationTime::operator==(const CombinationTime & other) const
{
    return time_mark.tm_year == other.time_mark.tm_year && time_mark.tm_mon == other.time_mark.tm_mon && time_mark.tm_mday == other.time_mark.tm_mday;
}
bool CombinationTime::operator!=(const CombinationTime & other) const
{
    return !operator==(other);
}
bool CombinationTime::operator>=(const CombinationTime & other) const
{
    return operator>(other) || operator==(other);
}
bool CombinationTime::operator<=(const CombinationTime & other) const
{
    return operator<(other) || operator==(other);
    ;
}
void CombinationTime::add_month(std::int32_t val)
{
    time_mark.tm_mon += val;
    if (time_mark.tm_mon > 11) {
        time_mark.tm_year += (time_mark.tm_mon / 12);
        time_mark.tm_mon %= 12;
    }
}
void CombinationTime::add_quartal(std::int32_t val)
{
    add_month(3 * val);
}
void CombinationTime::add_year(std::int32_t val)
{
    time_mark.tm_year += val;
}
void CombinationTime::add_day(std::int32_t val)
{
    time_mark.tm_mday += val;
    mktime(&time_mark);
}
