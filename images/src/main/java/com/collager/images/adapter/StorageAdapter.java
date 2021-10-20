package com.collager.images.adapter;

public interface StorageAdapter {
    String upload(String name, byte[] data);

    boolean remove(String id);
}
