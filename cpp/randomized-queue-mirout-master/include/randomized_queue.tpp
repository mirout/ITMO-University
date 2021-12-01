#pragma once

template <class T>
bool randomized_queue<T>::empty() const
{
    return m_data.empty();
}

template <class T>
std::size_t randomized_queue<T>::size() const
{
    return m_data.size();
}

template <class T>
std::size_t randomized_queue<T>::get_index() const
{
    auto distribution = std::uniform_int_distribution<std::size_t>(0, size() - 1);
    return distribution(generator);
}

template <class T>
void randomized_queue<T>::enqueue(T && value)
{
    m_data.emplace_back(std::move(value));
}

template <class T>
void randomized_queue<T>::enqueue(const T & value)
{
    m_data.push_back(value);
}

template <class T>
T const & randomized_queue<T>::sample() const
{
    return m_data[get_index()];
}

template <class T>
T randomized_queue<T>::dequeue()
{
    std::swap(m_data[get_index()], m_data.back());
    auto tmp = std::move(m_data.back());
    m_data.pop_back();
    return tmp;
}

template <class T>
typename randomized_queue<T>::iterator randomized_queue<T>::begin()
{
    return iterator(m_data.begin(), size());
}

template <class T>
typename randomized_queue<T>::iterator randomized_queue<T>::end()
{
    return iterator(m_data.begin(), size(), true);
}

template <class T>
typename randomized_queue<T>::const_iterator randomized_queue<T>::begin() const
{
    return const_iterator(m_data.begin(), size());
}

template <class T>
typename randomized_queue<T>::const_iterator randomized_queue<T>::end() const
{
    return const_iterator(m_data.begin(), size(), true);
}

template <class T>
typename randomized_queue<T>::const_iterator randomized_queue<T>::cbegin() const
{
    return const_iterator(m_data.cbegin(), size());
}

template <class T>
typename randomized_queue<T>::const_iterator randomized_queue<T>::cend() const
{
    return const_iterator(m_data.cbegin(), size(), true);
}