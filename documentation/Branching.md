# Branchingmodell

Gearbeitet wird jeweils in den Branches Phase-X. 
Sobald eine Phase abgeschlossen ist, wird ein Pullrequest für den Developmentbranch ausgelöst. 
Dieser muss von zwei Mitgliedern reviewed werden. Anschliessend darf der Pullrequest angenommen werden.
Im Developmentbranch wird nochmals mittels eines Jenkinsserver der Build getestet. Ist der Build erfolgreich, kann
von dort aus ein neuer Pullrequest für den Masterbranch ausgelöst werden. Diesen Pullrequest müssen alle Teammitglieder
reviewen. Anschliessend darf der Repoadmin den Pullrequest durchführen. Soll nun eine weitere Phase-X+1 erstellt werden
so basiert diese auf dem Developmentbranch und das Prozedere beginnt von vorne.