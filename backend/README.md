# Energy P2P Trading Application

## Overview

The Energy P2P (Peer-to-Peer) Trading Application is a decentralized system that allows users to trade energy among themselves. This system enables users to **buy** and **sell** energy units within the same grid. Each user has a **wallet** for holding funds and a **meter** for tracking energy consumption or generation. Additionally, the system is backed by a **Grid**, which can interact with users by purchasing excess energy from sellers at the end of the day.

## Running the application

1. Clone the application, at the root run

```
docker build . -t energy-p2p
docker compose up

```

## API Docs

Run the application and find the swagger api docs at

```
http://localhost:8080/swagger-ui/index.html
```

### Key Concepts:

1. **Buy Requests**: Users place buy orders for energy with a specified maximum price per unit and energy amount.
2. **Sell Requests**: Users place sell orders specifying the amount of energy they want to sell and the price per unit.
3. **Grid**: The Grid manages the remaining capacity and offers a buy price for any unresolved sell requests at the end of the day.

## Buy Request Process

1. **Place a Buy Request**:

   - The buyer places a buy request specifying:
     - `energyUnits`: The number of energy units they want to buy.
     - `maxPricePerUnit`: The maximum price they are willing to pay for each unit.
   - The system saves the buy request with a status of `ORDER_PLACED`.

2. **Matching Sell Requests**:
   - Once the buy request is placed, the system searches for matching **Sell Requests** that meet the following conditions:
     - The price per unit of the sell request is **less than or equal** to the `maxPricePerUnit` specified in the buy request.
     - The sell request has enough remaining energy units available for sale.
3. **Executing a Transaction**:
   - If a matching sell request is found, the transaction is executed:
     - The **buyer’s wallet** is debited, and the **seller’s wallet** is credited.
     - The **buyer’s meter** records the additional energy consumed, and the **seller’s meter** records the energy generated.
     - The sell request is updated to reflect the new amount of energy remaining. If all units are sold, the request status is marked as `COMPLETE`.
4. **Response**:
   - If the transaction is executed, the buyer receives a **Transaction ID** and other transaction details.
   - If no match is found, the buy request remains open with a status of `ORDER_PLACED` and is processed later if a matching sell request is placed.

---

## Sell Request Process

1. **Place a Sell Request**:

   - The seller places a sell request specifying:
     - `energyUnits`: The amount of energy to be sold.
     - `pricePerUnit`: The price at which they are willing to sell each unit.
   - The system saves the sell request with the status of `ORDER_PLACED` and stores the `remainingUnits`.

2. **Matching Buy Requests**:

   - Once the sell request is placed, the system searches for matching **Buy Requests** that meet the following conditions:
     - The `pricePerUnit` of the sell request is **less than or equal** to the `maxPricePerUnit` specified in the buy request.
     - The buy request has sufficient energy units required by the buyer.

3. **Executing a Transaction**:

   - If a matching buy request is found, the transaction is executed:
     - The **buyer’s wallet** is debited, and the **seller’s wallet** is credited.
     - The **buyer’s meter** records the additional energy consumed, and the **seller’s meter** records the energy generated.
     - The sell request’s remaining units are updated. If all units are sold, the request status is updated to `COMPLETE`.

4. **Response**:
   - If the transaction is successful, a **Transaction ID** is returned.
   - If no match is found, the sell request remains open with a status of `ORDER_PLACED` and is processed later when a matching buy request is placed.

---

## End-of-Day Grid Interaction

At the end of each day, the system ensures that unresolved buy and sell requests are handled:

1. **Selling to the Grid**:

   - Any unresolved sell requests (still in `ORDER_PLACED` status) are automatically sold to the **Grid**.
   - The sell requests are sold at the grid’s `buyPricePerUnit`, which is typically lower than the user-defined price.
   - The seller's **wallet** is credited for the remaining energy units, and the seller’s **meter** is updated to reflect the total energy sold.
   - The sell request is then marked as `COMPLETE`.

2. **Canceling Unresolved Buy Requests**:
   - Any unresolved buy requests that are still in `ORDER_PLACED` status at the end of the day are canceled.
   - These requests are marked as `CANCELLED` and will not be executed after the day ends.

### Grid Capacity

The grid has an ample remaining capacity to purchase unresolved sell requests. The remaining capacity is reduced as the grid buys energy from users.

---

## Data Models

### 1. **User**:

- Each user has:
  - A `wallet` for managing funds.
  - A `meter` for tracking energy consumption or generation.
  - A relationship with the `Grid` they are connected to.

### 2. **Grid**:

- The grid holds important properties like:
  - `buyPricePerUnit`: Price the grid pays for buying energy.
  - `sellPricePerUnit`: Price at which the grid sells energy.
  - `capacity`: Total capacity of the grid.
  - `remainingCapacity`: Amount of energy the grid can still buy.

### 3. **BuyRequest** and **SellRequest**:

- These entities track the details of buying and selling requests:
  - `energyUnits`: The number of energy units to buy/sell.
  - `pricePerUnit`: The price per unit of energy.
  - `status`: Status of the request (`ORDER_PLACED`, `COMPLETE`, etc.).
  - `timeStamp`: Time the request was placed.

### 4. **EnergyTransaction**:

- Tracks the transaction details when a buy and sell request is matched:
  - `buyer`: The buyer involved in the transaction.
  - `seller`: The seller involved.
  - `transactionAmount`: The total energy transferred.
  - `price`: Total price of the transaction.

---

## API Endpoints

### 1. **Buy Request**

- **POST** `/buy`
- Place a buy request with the following details:
  - `buyerId`: The ID of the buyer.
  - `energyUnits`: Number of units to buy.
  - `maxPricePerUnit`: Maximum price the buyer is willing to pay per unit.

**Response**:

- If a match is found, returns the `Transaction ID` and transaction details.
- If no match is found, returns `Order Placed`.

### 2. **Sell Request**

- **POST** `/sell`
- Place a sell request with the following details:
  - `sellerId`: The ID of the seller.
  - `energyUnits`: Number of units to sell.
  - `pricePerUnit`: Price per unit of energy.

**Response**:

- If a match is found, returns the `Transaction ID` and transaction details.
- If no match is found, returns `Order Placed`.

### 3. **End-of-Day Process**

- **POST** `/endOfDay`
- This endpoint processes all unresolved buy and sell requests at the end of the day:
  - Sell requests are sold to the grid.
  - Buy requests are canceled.

---

## Conclusion

The Energy P2P Trading Application allows users to seamlessly trade energy with each other while interacting with the grid. The system ensures fair trading based on price and energy availability, with the grid acting as the last resort to balance any unresolved sell requests at the end of the day. The application is highly configurable and scalable to handle a wide variety of trading scenarios.

---

This README provides a complete overview of the energy trading process for new users or developers to understand the core functionality of the system.
