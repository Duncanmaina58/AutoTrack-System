// queries.js
use("plp_bookstore");

// --------------------
// BASIC CRUD OPERATIONS
// --------------------

// 1. Find all books in a specific genre
db.books.find({ genre: "Mystery" });

// 2. Find books published after a certain year
db.books.find({ published_year: { $gt: 2018 } });

// 3. Find books by a specific author
db.books.find({ author: "Jane Doe" });

// 4. Update the price of a specific book
db.books.updateOne(
  { title: "The Silent Forest" },
  { $set: { price: 19.99 } }
);

// 5. Delete a book by its title
db.books.deleteOne({ title: "Lost Horizon" });

// --------------------
// ADVANCED QUERIES
// --------------------

// Books in stock and published after 2010
db.books.find({ in_stock: true, published_year: { $gt: 2010 } });

// Projection - show only title, author, price
db.books.find({}, { title: 1, author: 1, price: 1, _id: 0 });

// Sort by price ascending
db.books.find().sort({ price: 1 });

// Sort by price descending
db.books.find().sort({ price: -1 });

// Pagination (5 books per page)
db.books.find().skip(0).limit(5); // Page 1
db.books.find().skip(5).limit(5); // Page 2

// --------------------
// AGGREGATION PIPELINES
// --------------------

// Average price by genre
db.books.aggregate([
  { $group: { _id: "$genre", avgPrice: { $avg: "$price" } } }
]);

// Author with most books
db.books.aggregate([
  { $group: { _id: "$author", totalBooks: { $sum: 1 } } },
  { $sort: { totalBooks: -1 } },
  { $limit: 1 }
]);

// Group books by publication decade
db.books.aggregate([
  {
    $group: {
      _id: {
        $concat: [
          { $toString: { $subtract: [ "$published_year", { $mod: [ "$published_year", 10 ] } ] } },
          "s"
        ]
      },
      count: { $sum: 1 }
    }
  },
  { $sort: { _id: 1 } }
]);

// --------------------
// INDEXING
// --------------------

// Create index on title
db.books.createIndex({ title: 1 });

// Compound index on author and published_year
db.books.createIndex({ author: 1, published_year: -1 });

// Use explain() to demonstrate performance improvement
db.books.find({ title: "The Silent Forest" }).explain("executionStats");
