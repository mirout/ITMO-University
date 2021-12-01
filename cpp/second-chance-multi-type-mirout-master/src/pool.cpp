#include "pool.h"

#include <algorithm>
#include <vector>

namespace pool {

void * allocate(Pool * pool, std::size_t n)
{
    return pool->allocate(n);
}

void * Pool::allocate(const std::size_t n)
{
    auto pos = std::find(m_sizes.begin(), m_sizes.end(), n);
    if (pos != m_sizes.end()) {
        return m_blocks[pos - m_sizes.begin()]->allocate();
    }
    throw std::bad_alloc{};
}

void deallocate(Pool * pool, const void * ptr)
{
    pool->deallocate(ptr);
}

void Pool::deallocate(const void * ptr)
{
    for (auto & block : m_blocks) {
        if (block->is_own(ptr)) {
            block->deallocate(ptr);
            break;
        }
    }
}

void * Pool::Block::allocate()
{
    auto pos = std::find(m_used_map.begin(), m_used_map.end(), false);

    if (pos != m_used_map.end()) {
        *pos = true;
        return &m_storage[(pos - m_used_map.begin()) * m_obj_size];
    }

    throw std::bad_alloc{};
}

void Pool::Block::deallocate(const void * ptr)
{
    auto b_ptr = static_cast<const std::byte *>(ptr);
    m_used_map[(b_ptr - &m_storage[0]) / m_obj_size] = false;
}

inline bool Pool::Block::is_own(const void * ptr)
{
    return std::greater_equal{}(ptr, &m_storage[0]) && std::less_equal{}(ptr, &m_storage.back());
}

inline std::unique_ptr<Pool::Block> Pool::create_block(const std::size_t size, const std::size_t obj_size)
{
    return std::make_unique<Pool::Block>(size, obj_size);
}

Pool * create_pool(std::size_t count, std::initializer_list<std::size_t> sizes)
{
    return new Pool(count, sizes);
}
} // namespace pool