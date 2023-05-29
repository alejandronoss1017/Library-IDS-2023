package com.actors;

import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.utils.SocketUtil;
import com.utils.MessageUtil;
import com.server.db.BookOperations;
import com.server.db.BorrowOperations;
import com.server.model.*;

import io.github.cdimascio.dotenv.Dotenv;

public class BorrowActor {
    private static final String BORROW_TOPIC = Dotenv.load().get("BORROW_TOPIC");
    private static final String ACTOR_TO_LOAD_MANAGER_REPLY_PORT = Dotenv.load()
            .get("ACTOR_TO_LOAD_MANAGER_REPLY_PORT");

    private static final Logger logger = LoggerFactory.getLogger(BorrowActor.class);

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket replySocket = SocketUtil.connectSocket(context, SocketType.REP, "*",
                    ACTOR_TO_LOAD_MANAGER_REPLY_PORT,
                    true);

            logger.info("Borrow Actor running on port: " + ACTOR_TO_LOAD_MANAGER_REPLY_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                ZMsg msg = new ZMsg();

                msg = ZMsg.recvMsg(replySocket);

                BorrowActorWork(msg, replySocket);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void BorrowActorWork(ZMsg msg, ZMQ.Socket replySocket) {

        logger.info("Borrow Actor received: " + msg.toString());

        String service = msg.popString();
        String isbn = msg.popString();
        msg.clear();

        if (service.equals(BORROW_TOPIC)) {

            Book book = BookOperations.findByIsbn(isbn);

            if (book == null) {
                MessageUtil.sendMessage(replySocket, "Book not found");
            } else {
                Borrow borrow = new Borrow(book);

                try {
                    BorrowOperations.createBorrow(borrow);
                    MessageUtil.sendMessage(replySocket, book.toString());

                } catch (SQLIntegrityConstraintViolationException e) {
                    MessageUtil.sendMessage(replySocket, "Book already borrowed");
                    e.printStackTrace();
                }

            }

        } else {
            logger.info("Borrow Actor received message that is not a Borrow request");
            MessageUtil.sendMessage(replySocket, "Unknown message");
        }

    }
}
