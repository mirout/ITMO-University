#pragma once

#include "Combination.h"

#include <filesystem>
#include <memory>
#include <string>
#include <vector>

struct Component;

class Combinations
{
public:
    Combinations() = default;

    bool load(const std::filesystem::path & resource);

    std::string classify(const std::vector<Component> & components, std::vector<int> & order) const;

private:
    std::vector<std::unique_ptr<Combination>> combinations;
};
