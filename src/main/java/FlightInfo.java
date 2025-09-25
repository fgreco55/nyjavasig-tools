import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;

public class FlightInfo {

    public static void main(String[] args) {
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(OpenAiChatModelName.GPT_4_O)
                .strictTools(true)
                //.logRequests(true)
                //.logResponses(true)
                .build();

        ChatMemory cm = MessageWindowChatMemory.withMaxMessages(10);
// An effective system message that controls/constrains the method choices is critical.
        cm.add(SystemMessage.from("You are a helpful, and informative flight agent.  Only use the methods I have described."));  // just for illustrative purposes

        FlightAssistant assistant = AiServices.builder(FlightAssistant.class)
                .chatModel(model)
                .tools(new FlightInfoTools())
                .chatMemory(cm)
                .build();

        flightHelp(assistant, 10);  // calling it N times to illustrate GenAI answers may differ
    }

    private static void flightHelp(FlightAssistant infoAssistant, int iterations) {

        String response;

        for (int i = 0; i < iterations; i++) {
            response = infoAssistant.flightInfo("I need the status of Flight UA1011");
            System.out.println(response);

            response = infoAssistant.flightInfo("What type of aircraft is it?");
            System.out.println(response);

            response = infoAssistant.flightInfo("What was its cost");
            System.out.println(response);

            System.out.println("=========================");
        }
    }
}