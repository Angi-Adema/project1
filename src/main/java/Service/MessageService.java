package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import Model.Account;

public class MessageService {
    MessageDAO messageDAO = new MessageDAO();
    AccountDAO accountDAO = new AccountDAO();

    public Message createMessage(Message message) {

        Account poster = accountDAO.getAccountById(message.getPosted_by());

        if(poster == null) {
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageText(int messageId, String newText) {
        Message existing = messageDAO.getMessageById(messageId);

        if(existing == null) {
            return null;
        }

        existing.setMessage_text(newText);
        return messageDAO.updateMessage(existing);
    }
    
}
