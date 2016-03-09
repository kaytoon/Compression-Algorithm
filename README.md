# Compression-Algorithm
I have a large collection of raw text files of famous literature, including Leo Tolstoy’s War and Peace consisting of over 3 million characters and about 22000 different words (counting differences in capitalization), and store these works more efficiently. David Huffman developed a very efficient method for compressing data based on character frequency in a message. 
Since all the literature in my collection is in the English language it is reasonable to suppose they will not just have commonly used characters but commonly used words as well. My idea is that if i can treat every distinct word as a symbol and every non­word character (white space, punctuation, etc.) also as a symbol then i can apply Huffman’s encoding based on the frequency of words in the text instead of characters. I propose this will help us compress the text much smaller.
To carry this out we need an efficient way to store each word as we encounter it and the associated count it accumulates as we scan the entire document. While a binary search tree may work, the constant access time of a hash table is more attractive. I implement a hash table to help store the word counts and eventual codes for each word. 

A break down of how my code works:
● i create a table with 32768 (2^15) buckets.
● a hash function that for any key will produce an integer in the range [0...32767]
● implementing get and put methods (no need for remove) that hash a key and then
retrieve or store a value from the proper bucket.
● uses linear probing to handle collisions.
● counting the frequency of words and separators in a text file.
○ for the purposes of this project a word will count as string of characters from the set {0,...,9,A,...,Z,a,...,z,’,­}. Notice the ‘ and capital letters may appear in the string for words like “wouldn’t’ve” and “d’Eckmuhl”. Every other character is a separator and will be handled as a string of length one (i.e. “ “, “\n”, “!”, etc).
○ use a hashtable of my creation (see above) to store each word and separator in the hash table with its count.
● creating a tree with a single node for each word or separator with a non­zero count weighted by that count.
● i repeat the following step until there is only a single tree:
○ merge the two trees with minimum weight into a single tree with weight equal to
the sum of the two tree weights by creating a new root and adding the two trees
as left and right subtrees.
● labelling the single tree’s left branches with a 0 and right branches with a 1 and reading
the code for the strings stored in leaf nodes from the path from root to leaf.
● using the code for each string to create a compressed encoding of the message.
● (Optional) provide a method to decode the compressed message.
I implementing a Main controller that uses the CodingTree class to compress a file,
● Read the contents of a text file into a String.
● Pass the String into the CodingTree in order to initiate Huffman’s encoding procedure
and generate a map of codes.
● Output the codes to a text file.
● Output the compressed message to a binary file.
● Display compression and run time statistics.
