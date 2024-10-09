package com.bekn.energyp2p.util;

import org.sol4k.Base58;
import org.sol4k.Connection;
import org.sol4k.Keypair;
import org.sol4k.PublicKey;
import org.sol4k.RpcUrl;
import org.sol4k.TransactionMessage;
import org.sol4k.VersionedTransaction;
import org.sol4k.instruction.TransferInstruction;
import org.sol4k.rpc.RpcResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bekn.energyp2p.constants.ParentPaymentWallet;
import com.bekn.energyp2p.dto.WalletDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class BlockchainUtility {

    private final Connection connection;
    private final String solanaRpcUrl = "https://api.devnet.solana.com";

    public BlockchainUtility() {
        this.connection = new Connection(RpcUrl.DEVNET);
    }

    @Value("${user.lamportPerRupeee}")
    private Integer lamportPerRupeee;

    /**
     * Creates a new Solana blockchain wallet (keypair).
     * 
     * @return The generated WalletDTO with publicKey and secretKey.
     */
    public WalletDTO createWallet() {
        Keypair keypair = Keypair.generate();
        String publicKey = keypair.getPublicKey().toBase58();
        String secretKey = Base58.encode(keypair.getSecret());

        return new WalletDTO(publicKey, secretKey);
    }

    /**
     * Funds the user's wallet with 1 SOL from the parent wallet.
     * 
     * @param userWalletPublicKey The public key of the user's wallet.
     * @return The transaction signature if successful.
     * @throws Exception If the funding fails.
     */

    /**
     * Gets the balance of a given wallet.
     * 
     * @param publicKey The public key of the wallet.
     * @return The balance in Lamports.
     */

    public String fundUserWallet(String userWalletPublicKey, Double initailUserBalance) throws Exception {
        long amountInLamports = convertRupeesToLamports(initailUserBalance);
        String parentSecretKey = ParentPaymentWallet.getSecretKey();
        long parentWalletBalance = getWalletBalance(ParentPaymentWallet.getPublicKey());
        System.out.println(parentWalletBalance);
        if (parentWalletBalance > amountInLamports) {
            System.out.println("Parent wallet has sufficient balance. Parent balance: " + parentWalletBalance
                    + " Lamports. Funding user wallet now.");

            return transfer(parentSecretKey, userWalletPublicKey, amountInLamports);
        } else {
            System.out.println("Insufficient balance in parent wallet. Parent balance: " + parentWalletBalance
                    + " Lamports. Couldn't fund user wallet.");
            return "Insufficient Balance";
        }
    }

    /**
     * Transfers tokens (in Lamports) from one wallet to another.
     * 
     * @param senderSecretKey   The secret key of the sender's wallet.
     * @param receiverPublicKey The public key of the receiver's wallet.
     * @param amount            The amount to be transferred (in Lamports).
     * @return The transaction signature if successful.
     * @throws Exception If the transaction fails.
     */

    public String transfer(String senderSecretKey, String receiverPublicKey, long amount) throws Exception {
        try {
            System.out.println(
                    "Starting transfer of " + amount + " from " + senderSecretKey + " to " + receiverPublicKey);
            // Get latest blockhash
            var blockhash = connection.getLatestBlockhash();
            System.out.println("Blockhash: " + blockhash);

            var sender = Keypair.fromSecretKey(Base58.decode(senderSecretKey));
            var receiver = new PublicKey(receiverPublicKey);

            var transferInstruction = new TransferInstruction(sender.getPublicKey(), receiver, amount);

            var message = TransactionMessage.newMessage(sender.getPublicKey(), blockhash, transferInstruction);
            var transaction = new VersionedTransaction(message);
            transaction.sign(sender);

            var signature = connection.sendTransaction(transaction);
            System.out.println("Transaction Signature: " + signature);
            return signature;
        } catch (Exception e) {
            System.out.println("Blockchain transfer failed: " + e.getMessage());
            throw new Exception("Blockchain transfer failed: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the balance of a given wallet.
     * 
     * @param publicKey The public key of the wallet.
     * @return The balance in Lamports.
     */
    public long getWalletBalance(String publicKey) {
        try {
            PublicKey walletPublicKey = new PublicKey(publicKey);
            return connection.getBalance(walletPublicKey).longValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get wallet balance: " + e.getMessage(), e);
        }
    }

    /**
     * Gets transaction details by transaction signature.
     *
     * @param transactionSignature The signature of the transaction.
     * @return The transaction details.
     * @throws Exception If the transaction retrieval fails.
     */

    public JsonNode getTransactionDetails(String transactionSignature) throws Exception {
        try {
            System.out.println("Fetching transaction details for: " + transactionSignature);

            ObjectMapper objectMapper = new ObjectMapper();

            ArrayNode paramsArray = objectMapper.createArrayNode();
            paramsArray.add(transactionSignature);

            ObjectNode encodingNode = objectMapper.createObjectNode();
            encodingNode.put("encoding", "json");
            encodingNode.put("maxSupportedTransactionVersion", 0);
            paramsArray.add(encodingNode);

            ObjectNode requestPayload = objectMapper.createObjectNode();
            requestPayload.put("jsonrpc", "2.0");
            requestPayload.put("id", 1);
            requestPayload.put("method", "getTransaction");
            requestPayload.set("params", paramsArray);

            System.out.println("Request payload: " + requestPayload.toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(solanaRpcUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestPayload.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Failed to fetch transaction details: HTTP error code " + response.statusCode());
            }

            System.out.println("Response: " + response.body());

            JsonNode jsonResponse = objectMapper.readTree(response.body());

            return jsonResponse;
        } catch (Exception e) {
            System.out.println("Failed to fetch transaction details: " + e.getMessage());
            throw new Exception("Failed to fetch transaction details: " + e.getMessage(), e);
        }
    }

    /**
     * Converts SOL to Lamports.
     * 
     * @param sol The amount in SOL.
     * @return Equivalent value in Lamports.
     */
    public long convertSolToLamports(double sol) {
        return (long) (sol * 1_000_000_000L); // 1 SOL = 1,000,000,000 Lamports
    }

    public long convertRupeesToLamports(double rupee) {
        return (long) (rupee * lamportPerRupeee); // 1 Rupee = 100 Lamports
    }

    /**
     * Converts Lamports to SOL.
     * 
     * @param lamports The amount in Lamports.
     * @return Equivalent value in SOL.
     */
    public double convertLamportsToSol(long lamports) {
        return lamports / 1_000_000_000.0;
    }
}
