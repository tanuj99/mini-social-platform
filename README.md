# Mini Social Platform

## Overview

This project demonstrates various caching techniques for a simple social media feed system implemented in **Plain Java**. It includes custom implementations of **LRU Cache**, **LFU Cache**, **Cache Aside Pattern**, **Request Coalescing**, and a benchmark comparing LRU and LFU cache performance.

---

## Features

- LRU Cache implementation from scratch
- LFU Cache implementation from scratch
- Cache Aside pattern for feed retrieval
- Feed cache invalidation on new post creation
- Request Coalescing to prevent cache stampede
- Benchmark comparing LRU vs LFU
- Unit tests for LRU and LFU
- In-memory implementation (No external database)

---

## Project Structure

```
mini-social-platform
│
├── src
│
│   ├── cache
│   │      Cache.java
│   │      Node.java
│   │      LFUNode.java
│   │      DoublyLinkedList.java
│   │      LRUCache.java
│   │      LFUCache.java
│   │
│   ├── model
│   │      User.java
│   │      Post.java
│   │
│   ├── service
│   │      FeedService.java
│   │      PostService.java
│   │      RequestCoalescer.java
│   │
│   ├── benchmark
│   │      CacheBenchmark.java
│   │
│   ├── tests
│   │      LRUCacheTest.java
│   │      LFUCacheTest.java
│   │
│   └── Main.java
│
├── docs
│      07-caching.md
│
└── README.md
```

---

## Technologies Used

- Java 17 (or Java 11+)
- Collections Framework
- ConcurrentHashMap
- CompletableFuture
- ExecutorService

---

## How to Run

Clone the repository

```bash
git clone https://github.com/<your-username>/mini-social-platform.git
```

Navigate into the project

```bash
cd mini-social-platform
```

Compile

```bash
javac -d out src/**/*.java
```

Run

```bash
java -cp out Main
```

---

## Design Overview

The project simulates a simple social networking platform.

Each user can

- Follow users
- Create posts
- View their personalized feed

Feed generation follows the **Cache Aside Pattern**.

```
Client

      |

FeedService

      |

LRU Cache

      |

Cache Hit -------------> Return Feed

      |

Cache Miss

      |

Generate Feed

      |

Store in Cache

      |

Return Feed
```

---

## Cache Invalidation

Whenever a user creates a new post:

1. The post is stored.
2. The author's cached feed is invalidated.
3. Feed caches of all followers are invalidated.

This ensures users receive updated feeds on the next request.

---

## LRU Cache

Implemented using

- HashMap
- Doubly Linked List

Operations

| Operation | Complexity |
|-----------|------------|
| get | O(1) |
| put | O(1) |
| invalidate | O(1) |

Eviction policy

Least Recently Used item is removed when capacity is exceeded.

---

## LFU Cache

Implemented using

- HashMap
- Frequency Map
- Doubly Linked Lists

Operations

| Operation | Complexity |
|-----------|------------|
| get | O(1) |
| put | O(1) |
| invalidate | O(1) |

Eviction policy

Least Frequently Used item is removed first.

If multiple entries have the same frequency, the Least Recently Used entry among them is evicted.

---

## Cache Aside Pattern

Read Flow

```
Request Feed

      |

Check Cache

      |

Hit ----------> Return

      |

Miss

      |

Read Database

      |

Update Cache

      |

Return Response
```

---

## Request Coalescing

To prevent cache stampedes, multiple concurrent requests for the same cache key share a single computation.

Instead of allowing all requests to regenerate the same feed, the first request computes the result while the remaining requests wait for completion.

```
100 Requests

        |

Request Coalescer

        |

First Thread Computes

        |

Remaining Threads Wait

        |

Single Response Shared
```

---

## Benchmark

The benchmark simulates an **80/20 workload** where

- 20% of the users generate approximately 80% of requests.

The benchmark compares

- Cache Hits
- Cache Misses
- Hit Ratio

for both LRU and LFU implementations.

---

## Tests

The project includes unit tests covering

### LRU Cache

- Put
- Get
- Update Existing Key
- Eviction Order
- Invalidate
- Clear

### LFU Cache

- Frequency Updates
- Eviction
- LRU Tie Breaking
- Invalidate
- Clear

---

## Sample Output

```
FIRST FEED REQUEST

Cache Miss

--------------------------------

SECOND FEED REQUEST

Cache Hit

--------------------------------

NEW POST CREATED

Cache Invalidated

--------------------------------

NEXT FEED REQUEST

Cache Miss

--------------------------------

REQUEST COALESCING

Thread 1 received feed

Thread 2 received feed

Thread 3 received feed

...

Thread 10 received feed
```

---

## Future Improvements

- Redis Integration
- Time To Live (TTL)
- Distributed Cache
- Write Through Cache
- Write Back Cache
- Distributed Request Coalescing
- Kafka Based Cache Invalidation
- REST API using Spring Boot
- Persistent Database Support

---

## Author

Tanuj Soni
