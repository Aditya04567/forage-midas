// package com.jpmc.midascore.component;

// import com.jpmc.midascore.entity.TransactionRecord;
// import com.jpmc.midascore.entity.UserRecord;
// import com.jpmc.midascore.foundation.Incentive;
// import com.jpmc.midascore.foundation.Transaction;
// import com.jpmc.midascore.repository.TransactionRepository;
// import com.jpmc.midascore.repository.UserRepository;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Service;

// @Service
// public class TransactionService {

//     private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

//     private final UserRepository userRepository;
//     private final TransactionRepository transactionRepository;
//     private final IncentiveClient incentiveClient; // Added IncentiveClient

//     // Autowire IncentiveClient
//     public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, IncentiveClient incentiveClient) {
//         this.userRepository = userRepository;
//         this.transactionRepository = transactionRepository;
//         this.incentiveClient = incentiveClient;
//     }

//     public void process(Transaction transaction) {
//         UserRecord sender = userRepository.findById(transaction.getSenderId());
//         if (sender == null) return;

//         UserRecord recipient = userRepository.findById(transaction.getRecipientId());
//         if (recipient == null) return;

//         if (sender.getBalance() < transaction.getAmount()) return;

//         // Deduct from sender
//         sender.setBalance(sender.getBalance() - transaction.getAmount());

//         // Fetch incentive using the REST API client
//         Incentive incentive = incentiveClient.getIncentive(transaction);
//         float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

//         // Add transaction amount + incentive to recipient
//         recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

//         userRepository.save(sender);
//         userRepository.save(recipient);

//         // Save transaction record (ensure your TransactionRecord entity accepts the incentive amount in its constructor)
//         transactionRepository.save(new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount));
        
//         logger.info("Processed: {} -> {} £{} (Incentive: £{})", sender.getName(), recipient.getName(), transaction.getAmount(), incentiveAmount);

//         UserRecord waldorf = userRepository.findById(5L);
//         if (waldorf != null) {
//             logger.info(">>> WALDORF BALANCE: {}", waldorf.getBalance());
//         }
//     }
// }



package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

private static final Logger logger =
        LoggerFactory.getLogger(TransactionService.class);

private final UserRepository userRepository;
private final TransactionRepository transactionRepository;
private final IncentiveClient incentiveClient;

public TransactionService(UserRepository userRepository,
                          TransactionRepository transactionRepository,
                          IncentiveClient incentiveClient) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.incentiveClient = incentiveClient;
}

public void process(Transaction transaction) {

    UserRecord sender =
            userRepository.findById(transaction.getSenderId());

    if (sender == null) {
        return;
    }

    UserRecord recipient =
            userRepository.findById(transaction.getRecipientId());

    if (recipient == null) {
        return;
    }

    if (sender.getBalance() < transaction.getAmount()) {
        return;
    }

    Incentive incentive =
            incentiveClient.getIncentive(transaction);

    float incentiveAmount =
            incentive != null
                    ? incentive.getAmount()
                    : 0f;

    sender.setBalance(
            sender.getBalance()
                    - transaction.getAmount()
    );

    recipient.setBalance(
            recipient.getBalance()
                    + transaction.getAmount()
                    + incentiveAmount
    );

    userRepository.save(sender);
    userRepository.save(recipient);

    TransactionRecord record =
            new TransactionRecord(
                    sender,
                    recipient,
                    transaction.getAmount(),
                    incentiveAmount
            );

    transactionRepository.save(record);

    logger.info(
            "Processed: {} -> {} Amount={} Incentive={}",
            sender.getName(),
            recipient.getName(),
            transaction.getAmount(),
            incentiveAmount
    );
}
        

}