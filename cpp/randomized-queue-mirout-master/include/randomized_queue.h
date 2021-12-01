#pragma once

#include <cstddef>
#include <random>
#include <vector>

template <class T>
class randomized_queue
{
    template <class V>
    class base_iterator
    {
    private:
        using vector_iterator = std::conditional_t<std::is_const_v<V>,
                                                   typename std::vector<T>::const_iterator,
                                                   typename std::vector<T>::iterator>;
        vector_iterator m_begin;
        std::size_t m_current;
        std::vector<size_t> m_random_permutation;

    public:
        using value_type = V;
        using difference_type = std::ptrdiff_t;
        using pointer = value_type *;
        using reference = value_type &;
        using iterator_category = std::forward_iterator_tag;

        base_iterator() = default;
        base_iterator(vector_iterator begin, size_t size, bool is_end = false);

        reference operator*() const;
        pointer operator->() const;
        base_iterator & operator++();
        base_iterator operator++(int);
        bool operator==(const base_iterator & other) const;
        bool operator!=(const base_iterator & other) const;
    };

public:
    randomized_queue() = default;

    using iterator = base_iterator<T>;
    using const_iterator = base_iterator<const T>;

    bool empty() const;
    std::size_t size() const;
    void enqueue(T &&);
    void enqueue(const T &);
    T const & sample() const;
    T dequeue();
    iterator begin();
    iterator end();
    const_iterator begin() const;
    const_iterator end() const;
    const_iterator cbegin() const;
    const_iterator cend() const;

private:
    mutable std::mt19937_64 generator = std::mt19937_64{std::random_device{}()};
    std::size_t get_index() const;
    std::vector<T> m_data;
};

#include "randomized_queue.tpp"
#include "randomized_queue_iterator.tpp"