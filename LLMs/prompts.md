Some prooompts I have used to finetune and/or modify llm outputs. 

## Claude
Assign a role:
- You’re Jack, the best content writer in the world. Summarize the main ideas from the provided article text within the <text> tags

Learn a new topic using AI: 
- Prompt 1: You must always ask questions before you answer so you can better understand what the context of the question is.
- Prompt 2: I don't know [topic]. Provide a list of sub-topics that I can choose from to learn about.
- Hey Claude. I want to learn about [topic] in simple terms. Explain to me like I'm 11 years old.
- Expand on that and provide more context. Show me specific applications

Give Claude time to think: 
- When you generate the answer, first think how the output should be structured and add your answer in <thinking></thinking> tags. This is a space for you to write down relevant content and will not be shown to the user. Once you are done thinking, answer the question. Put your answer inside <answer></answer> XML tags.

Put long documents before instructions: 
- If you’re dealing with longer documents, always ask your question at the end of the prompt. For very long prompts Claude gives accent to the end of your prompt, so you need to add important instructions at the end.

Thinking step by step: 
- You can significantly improve the accuracy, by adding the phrase “Think step by step” that will force Claude to think step by step, and follow intermediate steps to arrive to the final answer. This is called zero-shot chain of thought prompting. 

## ChatGPT
For creating notes on information: 
- You are a great note taker, can you write notes on the following text I provide so that it has the relevant context and is concise -- so that in future if I come back to it, I won't be confused. 