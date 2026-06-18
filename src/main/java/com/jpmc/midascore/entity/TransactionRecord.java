package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    private float amount;
    
    // NEW FIELD: Stores the incentive amount fetched from the API
    private float incentive;

    public TransactionRecord() {
    }

    // UPDATED CONSTRUCTOR: Now accepts the 4th argument (incentive)
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) { this.sender = sender; }

    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) { this.recipient = recipient; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    // NEW GETTER AND SETTER
    public float getIncentive() { return incentive; }
    public void setIncentive(float incentive) { this.incentive = incentive; }
}

