#pragma once

#include <algorithm>

template <class T>
template <class V>
randomized_queue<T>::base_iterator<V>::base_iterator(vector_iterator begin, size_t size, bool is_end)
    : m_begin(begin)
    , m_current(is_end ? size : 0)
    , m_random_permutation(size)
{
    static std::mt19937_64 generator = std::mt19937_64{std::random_device{}()};
    std::iota(m_random_permutation.begin(), m_random_permutation.end(), 0);
    std::shuffle(m_random_permutation.begin(), m_random_permutation.end(), generator);
    m_random_permutation.push_back(size);
}

template <class T>
template <class V>
typename randomized_queue<T>::template base_iterator<V>::reference randomized_queue<T>::base_iterator<V>::operator*() const
{
    return *(m_begin + m_random_permutation[m_current]);
}

template <class T>
template <class V>
typename randomized_queue<T>::template base_iterator<V>::pointer randomized_queue<T>::base_iterator<V>::operator->() const
{
    return &(m_begin + m_random_permutation[m_current]);
}

template <class T>
template <class V>
typename randomized_queue<T>::template base_iterator<V> & randomized_queue<T>::base_iterator<V>::operator++()
{
    ++m_current;
    return *this;
}

template <class T>
template <class V>
typename randomized_queue<T>::template base_iterator<V> randomized_queue<T>::base_iterator<V>::operator++(int)
{
    auto tmp = *this;
    ++m_current;
    return tmp;
}

template <class T>
template <class V>
bool randomized_queue<T>::base_iterator<V>::operator==(const typename randomized_queue<T>::template base_iterator<V> & other) const
{
    return m_begin + m_random_permutation[m_current] == other.m_begin + other.m_random_permutation[other.m_current];
}

template <class T>
template <class V>
bool randomized_queue<T>::base_iterator<V>::operator!=(const typename randomized_queue<T>::template base_iterator<V> & other) const
{
    return !(operator==(other));
}