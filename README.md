# TransactionReconciliation

Reconciliation is the process of ensuring that two sets of records are in agreement. Banks often
implement reconciliation processes to ensure that data sent by one system is successfully consumed
and processed by another system without any discrepancies or breaks. This is done by making sure the
records in these two systems match at the end of a particular period.

Location of files: \src\main\java\resource  ..File1,File2

Inorder to make any changes to the file, please modify the file and rename the file as stated above.

Inorder to run the program:
1) Run the App.java present in \src\main\java\deepak\StandardChartered\App.java location.
2) open browser and run http://localhost:8080/

Process: 
1) File2 will be first loaded using InputStream into the HashMap with Key as combination of AccountId,PostingDate & Amount  and Value as TransactionID one at a time. (O(n) time complexity)
2) Now File1 is loaded into memory as InputStream and each file follows below step: (O(m) time complexity)
2.1) Transaction from file1 is checked in Map to see if any Exact Match is present. If present the match is stored in ExactMatch          response.
2.2)If Exact match is not found, we create 8 possible combination for the input which can act as weakMatcha and File2 map is checked for each combination.If match is found, match is stored in weakMatch.
2.3)If neither Exact nor Weak match is found, we we store the transaction from File1 into XBreaks reseponse.
3) After step 2 is processed for every transaction in File1, then store all Transasction from File2 Map which is not matched with any Transaction from File1 into YBreaks response.

Total time complexity of above solution is O(m+n) where m and n are the size of File1 and File2.
