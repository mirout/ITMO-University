#include "wordnet.h"

Outcast::Outcast(const WordNet & wordnet)
    : wordNet(wordnet)
{
}

std::string Outcast::outcast(const std::vector<std::string> & nouns)
{
    int max_dist = 0;
    bool equal = false;
    int pos = 0;
    for (size_t i = 0; i < nouns.size(); ++i) {
        int dist = 0;
        for (size_t j = 0; j < nouns.size(); ++j) {
            if (i != j) {
                dist += wordNet.distance(nouns[i], nouns[j]);
            }
        }
        if (max_dist < dist) {
            pos = i;
            max_dist = dist;
            equal = false;
        }
        else if (max_dist == dist) {
            equal = true;
        }
    }
    if (equal) {
        return "";
    }
    return nouns[pos];
}
