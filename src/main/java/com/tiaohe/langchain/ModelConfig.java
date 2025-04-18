package com.tiaohe.langchain;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    public interface Assistant {
        String chat(@MemoryId int memoryId, @UserMessage String userMessage);
    }

    public interface AssistantOfRAG {
        String chat(String userMessage);
    }

    // 创建一个持久化聊天记忆存储 Bean
    @Bean
    public ChatMemoryStore persistentChatMemoryStore() {
        return new PersistentChatMemoryStore();
    }

    // 创建一个聊天记忆提供者 Bean
    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore chatMemoryStore) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }

    // 创建一个聊天助手服务 Bean
    @Bean
    public Assistant assistant(ChatLanguageModel chatLanguageModel, ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

}
