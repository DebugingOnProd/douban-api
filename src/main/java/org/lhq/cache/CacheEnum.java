package org.lhq.cache;


import org.lhq.entity.book.BookInfo;

public enum CacheEnum {
    INSTANCE;


    private final CacheService<String, BookInfo> bookCacheService;


    CacheEnum() {
        this.bookCacheService = new CacheService<>();
    }

    public CacheService<String, BookInfo> getBookCacheService() {
        return bookCacheService;
    }

}
