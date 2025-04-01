package com.tiaohe.langchain;

import static com.tiaohe.langchain.PersistentChatMemoryStore.memoryMap;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
    final OpenAiChatModel openAiChatModel;
    final ModelConfig.Assistant assistant;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return openAiChatModel.chat(message);
    }

    // 处理聊天请求的接口
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {
        System.out.println(memoryMap);
        return assistant.chat(Integer.parseInt(request.get("memoryId")), request.get("userMessage"));
    }

    @PostMapping("/SystemChat")
    public String SystemChat(@RequestBody Map<String, String> request) {
        Friend friend = AiServices.create(Friend.class, openAiChatModel);
        return friend.SystemChat(request.get("message"));
    }

    @PostMapping("/userChat")
    public String userChat(@RequestBody Map<String, String> request) {
        Friend friend = AiServices.create(Friend.class, openAiChatModel);
        return friend.userChat(request.get("message"));
    }

    @PostMapping("/testRag")
    public String testRag(@RequestBody Map<String, String> request) {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/file");

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        ModelConfig.AssistantOfRAG assistant = AiServices.builder(ModelConfig.AssistantOfRAG.class)
            .chatLanguageModel(openAiChatModel).chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore)).build();

        return assistant.chat(request.get("message"));
    }
}