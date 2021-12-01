#pragma once

#include <algorithm>
#include <fstream>
#include <queue>
#include <set>
#include <sstream>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

class Digraph
{
public:
    Digraph() = default;
    void add_edge(size_t from, size_t to);
    void create_vertex(size_t V);
    const std::vector<size_t> & get_edges(size_t V) const;
    std::ostream & operator<<(std::ostream & os) const;

private:
    std::unordered_map<size_t, std::vector<size_t>> adjacency_list;
};

class ShortestCommonAncestor
{
public:
    ShortestCommonAncestor(const Digraph & dg);

    // calculates length of shortest common ancestor path from node with id 'v' to node with id 'w'
    int length(int v, int w) const;

    // returns node id of shortest common ancestor of nodes v and w
    int ancestor(int v, int w) const;

    // calculates length of shortest common ancestor path from node subset 'subset_a' to node subset 'subset_b'
    int length_subset(const std::set<int> & subset_a, const std::set<int> & subset_b) const;

    // returns node id of shortest common ancestor of node subset 'subset_a' and node subset 'subset_b'
    int ancestor_subset(const std::set<int> & subset_a, const std::set<int> & subset_b) const;

private:
    void bfs(std::queue<std::pair<size_t, int>> & queue,
             std::unordered_map<size_t, bool> & used,
             std::unordered_map<size_t, bool> & used_another,
             std::unordered_map<size_t, int> & dists,
             std::unordered_map<size_t, int> & dists_another,
             const int & dist,
             int & min_dist,
             int & ancestor) const;

    std::pair<int, int> parallel_bfs(const std::set<int> & vs, const std::set<int> & ws) const;
    const Digraph & m_dag;
};

class WordNet
{
public:
    WordNet(const std::string & synsets_fn, const std::string & hypernyms_fn);

    class iterator
    {
        using map_iterator = std::unordered_map<std::string, std::vector<size_t>>::iterator;
        map_iterator iter;

    public:
        using iterator_category = std::forward_iterator_tag;
        using value_type = std::string;
        using difference_type = std::ptrdiff_t;
        using pointer = const value_type *;
        using reference = const value_type &;

        iterator(map_iterator m)
            : iter(m)
        {
        }
        iterator() = default;

        reference operator*();
        pointer operator->();
        iterator & operator++();
        iterator operator++(int);
        bool operator==(const iterator & other) const;
        bool operator!=(const iterator & other) const;
    };

    // get iterator to list all nouns stored in WordNet
    iterator nouns();
    iterator begin();
    iterator end();

    // returns 'true' iff 'word' is stored in WordNet
    bool is_noun(const std::string & word) const;

    // returns gloss of "shortest common ancestor" of noun1 and noun2
    std::string sca(const std::string & noun1, const std::string & noun2) const;

    // calculates distance between noun1 and noun2
    int distance(const std::string & noun1, const std::string & noun2) const;

private:
    //synset to gloss
    std::unordered_map<size_t, std::string> m_synsets_glosses;
    //noun to synsets
    std::unordered_map<std::string, std::vector<size_t>> m_noun_synsets;
    ShortestCommonAncestor m_shortest_common_ancestor;
    Digraph m_dag;
};

class Outcast
{
public:
    Outcast(const WordNet & wordnet);

    // returns outcast word
    std::string outcast(const std::vector<std::string> & nouns);

private:
    const WordNet & wordNet;
};