#include "subset.h"

#include "randomized_queue.h"

void subset(unsigned long k, std::istream & in, std::ostream & out)
{
    randomized_queue<std::string> r_queue;
    std::string line;
    while (std::getline(in, line)) {
        r_queue.enqueue(line);
    }
    while (k-- > 0 && !r_queue.empty()) {
        out << r_queue.dequeue() << std::endl;
    }
}
