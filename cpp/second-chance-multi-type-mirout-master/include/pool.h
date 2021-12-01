#pragma once

#include <cstddef>
#include <initializer_list>
#include <memory>
#include <new>
#include <vector>

namespace pool {
class Pool
{
    class Block
    {
    public:
        Block(const size_t block_size, const size_t obj_size)
            : m_obj_size(obj_size)
            , m_storage(block_size)
            , m_used_map(block_size / obj_size, false){};

        void * allocate();
        void deallocate(const void *);
        bool is_own(const void *);

    private:
        static constexpr size_t npos = static_cast<size_t>(-1);
        std::_Bit_iterator find_empty_place();

        const size_t m_obj_size;
        std::vector<std::byte> m_storage;
        std::vector<bool> m_used_map;
    };

public:
    Pool(const std::size_t count, std::initializer_list<std::size_t> sizes)
        : m_sizes(sizes)
        , m_blocks(sizes.size())
    {
        for (std::size_t i = 0; i < m_sizes.size(); i++) {
            m_blocks[i] = create_block(count, m_sizes[i]);
        }
    }

    void * allocate(std::size_t n);
    void deallocate(const void * ptr);

private:
    std::unique_ptr<Pool::Block> create_block(std::size_t size, std::size_t obj_size);

    std::vector<std::size_t> m_sizes;
    std::vector<std::unique_ptr<Block>> m_blocks;
};

Pool * create_pool(std::size_t count, std::initializer_list<std::size_t> sizes);
void * allocate(Pool * pool, std::size_t n);
void deallocate(Pool * pool, const void *);

} // namespace pool
class PoolAllocator
{
public:
    PoolAllocator(const std::size_t count, std::initializer_list<std::size_t> sizes)
        : m_pool(pool::create_pool(count, sizes))
    {
    }

    void * allocate(const std::size_t n)
    {
        return pool::allocate(m_pool.get(), n);
    }

    void deallocate(const void * ptr)
    {
        pool::deallocate(m_pool.get(), ptr);
    }

private:
    std::unique_ptr<pool::Pool> m_pool;
};
