package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context ctx) {
        // Javalin internally uses Jackson's ObjectMapper and abstracts away the boilerplate so we do not have to write or confiture this.
        // We also do not need to specify the throws JsonProcessingException as Javalin will handle this for us.
        Account acct = ctx.bodyAsClass(Account.class);

        if(acct.getUsername() == null || acct.getUsername().isBlank() || acct.getPassword() == null || acct.getPassword().length() < 4) {
            ctx.status(400).result("Username cannot be blank and password must be at least 4 characters");
            return;
        }
        // The following code is commented out because it is not implemented yet. You will need to implement the register method in the AccountService class.
        try {
            Account newAcct = accountService.register(acct);
            if (newAcct != null) {
                // Javalin will automatically return the response as 200 OK as the default along with the JSON object.
                ctx.status(200).json(newAcct);
            } else {
                ctx.status(400).result("Account already exists");
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error: " + e.getMessage());
            e.printStackTrace();
        }

    }
    
    private void loginHandler(Context ctx) {

        Account loginAttempt = ctx.bodyAsClass(Account.class);

        if(loginAttempt.getUsername() == null || loginAttempt.getUsername().isBlank() || loginAttempt.getPassword() == null || loginAttempt.getPassword().isBlank()) {
            ctx.status(400);
            return;
        }

        try {
            Account existingAccount = accountService.login(loginAttempt.getUsername(), loginAttempt.getPassword());

            if(existingAccount != null) {
                ctx.status(200).json(existingAccount);
            } else {
                ctx.status(401).result("Invalid username or password");
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void messageHandler(Context ctx) {

        Message message = ctx.bodyAsClass(Message.class);

        if(message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 || message.getPosted_by() <= 0) {
            ctx.status(400).result("Message cannot be blank and must be less than 255 characters");
            return;
        }
        Message created = messageService.createMessage(message);
        
        if(created != null) {
            ctx.json(created);
        } else {
            ctx.status(400).result("Message could not be created");
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);

            if(message != null) {
                ctx.json(message);
            } else {
                ctx.json(" ").result("Message not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID format");
        } catch (Exception e) {
            ctx.status(500).result("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted = messageService.deleteMessageById(messageId);

            if(deleted != null) {
                ctx.json(deleted);
            } else {
                ctx.json(" ").result("Message not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID format"); 
        }
    }

    private void updateMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updateRequest = ctx.bodyAsClass(Message.class);

            if(updateRequest.getMessage_text() == null || updateRequest.getMessage_text().isBlank() || updateRequest.getMessage_text().length() >= 255) {
                ctx.status(400).result("Message cannot be blank and must be less than 255 characters");
                return;
            }
            Message updatedMessage = messageService.updateMessageText(messageId, updateRequest.getMessage_text());

            if(updatedMessage != null) {
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID format");
        }
    }
}



