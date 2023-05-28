package com.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMsg;

import com.messageQueue.MessageQueue;
import com.server.db.BorrowOperations;
import com.server.model.Borrow;

import io.github.cdimascio.dotenv.Dotenv;

public class DoWorkThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DoWorkThread.class);
    private MessageQueue messageQueue;

    public DoWorkThread(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            try {
                ZMsg msg = messageQueue.consume();

                // Do database work
                dbOperation(msg);

            } catch (InterruptedException e) {
                logger.error("Error: " + e.getMessage());
                Thread.currentThread().interrupt();

            }
        }
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public static void dbOperation(ZMsg msg) {

        logger.info("Working on: " + msg.toString());

        String service = msg.popString();
        String isbn = msg.popString();

        Borrow borrow = BorrowOperations.findBorrowByIsbn(isbn);

        if (borrow != null && service.equals(Dotenv.load().get("RENEWAL_TOPIC"))) {
            BorrowOperations.renewalFinalDate(borrow);
            logger.info("Borrow renewed: " + isbn);
        } else if (borrow != null && service.equals(Dotenv.load().get("RETURN_TOPIC"))) {
            BorrowOperations.returnBorrow(borrow);
            logger.info("Book returned: " + isbn);
        } else {
            logger.error("Borrow not found: " + isbn);

        }

        msg.clear();
    }

}
