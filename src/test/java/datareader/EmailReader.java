package datareader;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.util.Properties;

public class EmailReader {
    private String gmailHost = "imap.gmail.com";
    private String gmailStore = "imaps";

    private String gmailUser;
    private String gmailPassword;
    private Properties gmailProps;


    public EmailReader(String email, String password) {
        gmailProps =  new Properties();
        gmailProps.put("mail.imap.host", gmailHost);
        gmailProps.put("mail.imap.port", "993");
        gmailProps.put("mail.imap.starttls.enable", "true");
        gmailProps.put("mail.imap.ssl.trust", gmailHost);

        gmailUser = email;
        gmailPassword = password;
    }

    public boolean clearMailbox() {
        Session emailSession = Session.getDefaultInstance(gmailProps);

        try {
            Store store = emailSession.getStore(gmailStore);
            store.connect(gmailHost, gmailUser, gmailPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.getMessages();

            for (Message msg : messages) {
                msg.setFlag(Flags.Flag.DELETED, true);
            }

            emailFolder.close(true);
            store.close();

            return true;
        }
        catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Message[] getEmailsBySubject(final String subject) {
        Session emailSession = Session.getDefaultInstance(gmailProps);

        try {
            Store store = emailSession.getStore(gmailStore);
            store.connect(gmailHost, gmailUser, gmailPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            SearchTerm subjectTerm = new SubjectTerm(subject);

            Message[] messages = emailFolder.search(subjectTerm);

            emailFolder.close(false);
            store.close();

            return messages;
        }
        catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
