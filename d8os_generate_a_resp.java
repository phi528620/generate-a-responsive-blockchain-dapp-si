import java.util.*;

public class dAppSimulator {
    // Data model for a blockchain
    static class Block {
        String blockHash;
        int blockSize;
        String previousBlockHash;
        List<Transaction> transactions;
    }

    // Data model for a transaction
    static class Transaction {
        String sender;
        String receiver;
        double amount;
        String transactionHash;
    }

    // Data model for a user's wallet
    static class Wallet {
        String userId;
        double balance;
        List<Transaction> transactionHistory;
    }

    // Data model for a responsive dApp simulator
    static class dAppSimulatorConfig {
        int blockSizeLimit;
        int blockInterval; // in seconds
        int numUsers;
        List<Wallet> wallets;
        List<Block> blockchain;
    }

    public static void main(String[] args) {
        // Initialize simulator config
        dAppSimulatorConfig config = new dAppSimulatorConfig();
        config.blockSizeLimit = 1000;
        config.blockInterval = 60;
        config.numUsers = 10;

        // Initialize users and wallets
        config.wallets = new ArrayList<>();
        for (int i = 0; i < config.numUsers; i++) {
            Wallet wallet = new Wallet();
            wallet.userId = "User " + (i + 1);
            wallet.balance = 1000.0;
            wallet.transactionHistory = new ArrayList<>();
            config.wallets.add(wallet);
        }

        // Initialize blockchain
        config.blockchain = new ArrayList<>();

        // Simulate blockchain and transactions
        while (true) {
            // Generate new block
            Block block = new Block();
            block.blockHash = generateHash();
            block.previousBlockHash = config.blockchain.size() > 0 ? config.blockchain.get(config.blockchain.size() - 1).blockHash : "";
            block.transactions = new ArrayList<>();

            // Generate transactions for the block
            for (int i = 0; i < config.blockSizeLimit; i++) {
                Transaction transaction = new Transaction();
                transaction.sender = getRandomUser(config.wallets).userId;
                transaction.receiver = getRandomUser(config.wallets).userId;
                transaction.amount = Math.random() * 100;
                transaction.transactionHash = generateHash();
                block.transactions.add(transaction);
            }

            // Add block to blockchain
            config.blockchain.add(block);

            // Update user balances
            for (Transaction transaction : block.transactions) {
                for (Wallet wallet : config.wallets) {
                    if (wallet.userId.equals(transaction.sender)) {
                        wallet.balance -= transaction.amount;
                    } else if (wallet.userId.equals(transaction.receiver)) {
                        wallet.balance += transaction.amount;
                    }
                }
            }

            // Print blockchain state
            System.out.println("Blockchain state:");
            for (Block b : config.blockchain) {
                System.out.println("  Block " + b.blockHash + " - " + b.transactions.size() + " transactions");
            }

            // Wait for block interval
            try {
                Thread.sleep(config.blockInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper functions
    static String generateHash() {
        return UUID.randomUUID().toString();
    }

    static Wallet getRandomUser(List<Wallet> wallets) {
        Random random = new Random();
        return wallets.get(random.nextInt(wallets.size()));
    }
}