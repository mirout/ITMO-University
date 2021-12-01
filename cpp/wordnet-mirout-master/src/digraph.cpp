#include "wordnet.h"

void Digraph::add_edge(size_t from, size_t to)
{
    adjacency_list[from].push_back(to);
}

void Digraph::create_vertex(size_t V)
{
    adjacency_list.insert({V, {}});
}

const std::vector<size_t> & Digraph::get_edges(size_t V) const
{
    auto f = adjacency_list.find(V);
    return f->second;
}

std::ostream & Digraph::operator<<(std::ostream & os) const
{
    for (const auto & [key, vec] : adjacency_list) {
        os << key << " : ";
        for (const auto elem : vec) {
            os << elem << ' ';
        }
        os << '\n';
    }
    return os;
}