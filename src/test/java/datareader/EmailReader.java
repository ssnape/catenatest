/**
 *  The EmailReader class provides functionality related to processing
 *  emails. Currently it works with Gmail and using IMAP.
 *
 * @author Arielle Bonnici
 * @version 1.0
 * @since 2020-09-04
 */

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
        //Configure Gmail properties
        gmailProps =  new Properties();
        gmailProps.put("mail.imap.host", gmailHost);
        gmailProps.put("mail.imap.port", "993");
        gmailProps.put("mail.imap.starttls.enable", "true");
        gmailProps.put("mail.imap.ssl.trust", gmailHost);

        gmailUser = email;
        gmailPassword = password;
    }

    /**
     * Get all emails from Gmail inbox and delete them.
     *
     * @return True if no errors were encountered, false if there are errors
     */
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

    /**
     * Get emails from a Gmail mailbox where subject matches a specified
     * search string.
     *
     * @param subject String to search for in subject
     * @return A Message list containing emails with matching subject
     */
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
