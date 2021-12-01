//
// Created by Mikhail Ruzavin on 12.05.2021.
//

#include "wordnet.h"

#include <iostream>
#include <regex>

WordNet::iterator::reference WordNet::iterator::operator*()
{
    return iter->first;
}

WordNet::iterator::pointer WordNet::iterator::operator->()
{
    return &iter->first;
}

WordNet::iterator WordNet::iterator::operator++(int)
{
    auto tmp = *this;
    ++iter;
    return tmp;
}

WordNet::iterator & WordNet::iterator::operator++()
{
    ++iter;
    return *this;
}

bool WordNet::iterator::operator==(const WordNet::iterator & other) const
{
    return iter == other.iter;
}

bool WordNet::iterator::operator!=(const WordNet::iterator & other) const
{
    return !(iter == other.iter);
}

WordNet::WordNet(const std::string & synsets_fn, const std::string & hypernyms_fn)
    : m_shortest_common_ancestor(m_dag)
{
    std::ifstream synsets(synsets_fn);
    std::ifstream hypernyms(hypernyms_fn);

    if (!synsets.is_open()) {
        std::cerr << "Couldn't open the file: " << synsets_fn << std::endl;
        return;
    }
    if (!hypernyms.is_open()) {
        std::cerr << "Couldn't open the file: " << hypernyms_fn << std::endl;
        return;
    }

    std::string line;

    std::regex delimiter{','};
    std::regex whitespace{' '};

    while (std::getline(synsets, line)) {
        if (line.empty()) {
            continue;
        }

        std::sregex_token_iterator tokenIterator{line.cbegin(), line.cend(), delimiter, -1};

        size_t id = std::stoul(*tokenIterator++);
        std::string nouns = *tokenIterator++;
        std::string gloss = *tokenIterator;

        m_synsets_glosses[id] = gloss;

        std::sregex_token_iterator nounIterator{nouns.cbegin(), nouns.cend(), whitespace, -1};

        while (nounIterator != std::sregex_token_iterator{}) {
            m_noun_synsets[*nounIterator].push_back(id);
            ++nounIterator;
        }
    }

    while (std::getline(hypernyms, line)) {
        if (line.empty()) {
            continue;
        }

        std::sregex_token_iterator tokenIterator{line.cbegin(), line.cend(), delimiter, -1};
        size_t from = std::stoul(*tokenIterator++);
        m_dag.create_vertex(from);
        while (tokenIterator != std::sregex_token_iterator()) {
            m_dag.add_edge(from, std::stoul(*tokenIterator));
            ++tokenIterator;
        }
    }
}

bool WordNet::is_noun(const std::string & word) const
{
    return m_noun_synsets.find(word) != m_noun_synsets.cend();
}
WordNet::iterator WordNet::nouns()
{
    return begin();
}
WordNet::iterator WordNet::begin()
{
    return WordNet::iterator(m_noun_synsets.begin());
}
WordNet::iterator WordNet::end()
{
    return WordNet::iterator(m_noun_synsets.end());
}

int WordNet::distance(const std::string & noun1, const std::string & noun2) const
{
    auto & syns1 = m_noun_synsets.find(noun1)->second;
    auto & syns2 = m_noun_synsets.find(noun2)->second;
    return m_shortest_common_ancestor.length_subset({syns1.begin(), syns1.end()}, {syns2.begin(), syns2.end()});
}

std::string WordNet::sca(const std::string & noun1, const std::string & noun2) const
{
    auto & syns1 = m_noun_synsets.find(noun1)->second;
    auto & syns2 = m_noun_synsets.find(noun2)->second;
    return m_synsets_glosses.find(m_shortest_common_ancestor.ancestor_subset({syns1.begin(), syns1.end()}, {syns2.begin(), syns2.end()}))->second;
}
