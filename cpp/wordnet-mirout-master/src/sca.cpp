#include "wordnet.h"

#include <limits>

ShortestCommonAncestor::ShortestCommonAncestor(const Digraph & dg)
    : m_dag(dg)
{
}

void ShortestCommonAncestor::bfs(std::queue<std::pair<size_t, int>> & queue,
                                 std::unordered_map<size_t, bool> & used,
                                 std::unordered_map<size_t, bool> & used_another,
                                 std::unordered_map<size_t, int> & dists,
                                 std::unordered_map<size_t, int> & dists_another,
                                 const int & dist,
                                 int & min_dist,
                                 int & ancestor) const
{
    while (!queue.empty() && queue.front().second == dist) {
        size_t from = queue.front().first;
        queue.pop();
        for (const auto & to : m_dag.get_edges(from)) {
            if (used_another[to]) {
                if (min_dist > dists_another[to] + dist + 1) {
                    min_dist = dists_another[to] + dist + 1;
                    ancestor = to;
                }
            }
            if (!used[to]) {
                queue.emplace(to, dist + 1);
                used[to] = true;
                dists[to] = dist + 1;
            }
        }
    }
}

std::pair<int, int> ShortestCommonAncestor::parallel_bfs(const std::set<int> & vs, const std::set<int> & ws) const
{
    std::vector<int> intersection;
    std::set_intersection(vs.begin(), vs.end(), ws.begin(), ws.end(), std::back_inserter(intersection));

    if (!intersection.empty()) {
        return {intersection.front(), 0};
    }

    std::unordered_map<size_t, bool> used_v;
    std::unordered_map<size_t, bool> used_w;

    std::unordered_map<size_t, int> dist_v;
    std::unordered_map<size_t, int> dist_w;

    std::queue<std::pair<size_t, int>> queue_v;
    std::queue<std::pair<size_t, int>> queue_w;

    int dist = 0;

    for (const auto v : vs) {
        queue_v.emplace(v, dist);
        dist_v[v] = dist;
        used_v[v] = true;
    }
    for (const auto w : ws) {
        queue_w.emplace(w, dist);
        dist_w[w] = dist;
        used_w[w] = true;
    }

    int ancestor = 0;
    int min_dist = std::numeric_limits<int>::max();

    while (!queue_v.empty() || !queue_w.empty()) {
        bfs(queue_v, used_v, used_w, dist_v, dist_w, dist, min_dist, ancestor);
        bfs(queue_w, used_w, used_v, dist_w, dist_v, dist, min_dist, ancestor);
        dist++;
    }
    return {ancestor, min_dist};
}

int ShortestCommonAncestor::length(int v, int w) const
{
    return parallel_bfs({v}, {w}).second;
}

int ShortestCommonAncestor::ancestor(int v, int w) const
{
    return parallel_bfs({v}, {w}).first;
}

int ShortestCommonAncestor::length_subset(const std::set<int> & subset_a, const std::set<int> & subset_b) const
{
    return parallel_bfs(subset_a, subset_b).second;
}

int ShortestCommonAncestor::ancestor_subset(const std::set<int> & subset_a, const std::set<int> & subset_b) const
{
    return parallel_bfs(subset_a, subset_b).first;
}