package com.tiaohe.langchain;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Friend {
    @SystemMessage("You are a good friend of mine. Answer using slang.")
    String SystemChat(String userMessage);

    @UserMessage("You are a good friend of mine. Answer using slang. {{message}}")
    String userChat(@V("message") String userMessage);
}
