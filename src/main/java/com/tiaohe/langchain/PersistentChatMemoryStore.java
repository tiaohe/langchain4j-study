package com.tiaohe.langchain;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 持久化聊天记忆存储类，使用 HashMap 保存在内存中
class PersistentChatMemoryStore implements ChatMemoryStore {
    public static final Map<Integer, List<ChatMessage>> memoryMap = new HashMap<>();

    // 根据记忆 ID 获取聊天消息列表
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return memoryMap.getOrDefault((int) memoryId, List.of());
    }

    // 更新指定记忆 ID 的聊天消息列表
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        memoryMap.put((int) memoryId, messages);
    }

    // 删除指定记忆 ID 的聊天消息
    @Override
    public void deleteMessages(Object memoryId) {
        memoryMap.remove((int) memoryId);
    }
}