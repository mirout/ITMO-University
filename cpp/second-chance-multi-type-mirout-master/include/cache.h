#pragma once

#include <cstddef>
#include <list>
#include <new>
#include <ostream>

template <class Key, class KeyProvider, class Allocator>
class Cache
{
public:
    template <class... AllocArgs>
    Cache(const std::size_t cache_size, AllocArgs &&... alloc_args)
        : m_max_size(cache_size)
        , m_alloc(std::forward<AllocArgs>(alloc_args)...)
    {
    }

    std::size_t size() const
    {
        return m_queue.size();
    }

    bool empty() const
    {
        return m_queue.empty();
    }

    template <class T>
    T & get(const Key & key);

    std::ostream & print(std::ostream & strm) const;

    friend std::ostream & operator<<(std::ostream & strm, const Cache & cache)
    {
        return cache.print(strm);
    }

private:
    const std::size_t m_max_size;
    Allocator m_alloc;
    std::list<std::pair<void *, bool>> m_queue;
};

template <class Key, class KeyProvider, class Allocator>
template <class T>
inline T & Cache<Key, KeyProvider, Allocator>::get(const Key & key)
{
    for (auto & elem : m_queue) {
        if (*static_cast<KeyProvider *>(elem.first) == key) {
            elem.second = true;
            return *static_cast<T *>(elem.first);
        }
    }
    while (m_queue.size() >= m_max_size) {
        auto pair = m_queue.front();
        m_queue.pop_front();
        if (pair.second) {
            pair.second = false;
            m_queue.emplace_back(pair);
        }
        else {
            m_alloc.template destroy<KeyProvider>(pair.first);
        }
    }
    m_queue.emplace_back(m_alloc.template create<T>(key), false);
    return *static_cast<T *>(m_queue.back().first);
}

template <class Key, class KeyProvider, class Allocator>
inline std::ostream & Cache<Key, KeyProvider, Allocator>::print(std::ostream & strm) const
{
    if (m_queue.empty()) {
        return strm << "<empty>";
    }
    for (auto & elem : m_queue) {
        strm << *static_cast<KeyProvider *>(elem.first) << " ";
    }
    return strm;
}
