# Branchingmodell

Gearbeitet wird jeweils in den Branches Phase-X. 
Sobald eine Phase abgeschlossen ist, wird ein Pullrequest für den Developmentbranch ausgelöst. 
Dieser muss von zwei Mitgliedern reviewed werden. Anschliessend darf der Pullrequest angenommen werden.
Im Developmentbranch wird nochmals mittels eines Jenkinsserver der Build getestet. Ist der Build erfolgreich, kann
von dort aus ein neuer Pullrequest für den Masterbranch ausgelöst werden. Anschliessend darf der Repoadmin den Pullrequest durchführen. Soll nun eine weitere Phase-X+1 erstellt werden
so basiert diese auf dem Developmentbranch und das Prozedere beginnt von vorne.

# Definition of Done

Die Definition of Done sieht bei uns folgendermassen aus. Wenn ein neues Feature eingebaut wird, wird dazu ein GitHub 
Projects Issue erstellt mit einem passenden und beschreibenden Titel und wenn notwendig einem Label. Dieses befindet 
sich dann im Backlog. Wenn ein Mitglied das umsetzen will, assigned er sich und schiebt es in "In Progress", wenn er
mit der Bearbeitung beginnt. Somit können die anderen Mitglieder sehen, wer an was arbeitet. Ist eine erste Version
davon lauffähig kann es in "In Testing" geschoben werden. In dieser Spalte wird es meistens von Hand explorativ getestet
und mit Teamkollegen angeschaut. In dieser Phase spricht man auch über entsprechenden unit tests und erstellt die 
dazugehörigen Issues. Wenn das Issue vollständig implementiert ist und das Mitglied, damit zufrieden ist wird es in 
"In Review" geschoben. Dort muss es von mindestens zwei anderen reviewed und entsprechend kommentiert werden. Die Commits
werden mit den Issues verlinkt, wenn es gepusht wird. Ist das Issue reviewed kann es vom 2. Reviewer geschlossen werden.
Dann endet es automatisch in der "Done" Spalte. Ausnahme zu diesem Workflow gibt es, wenn Reviews direkt mündlich stattfinden. 
Dann werden die Kommentare kurzgefasst, aber dennoch hingeschrieben, dass das auch für aussenstehende ersichtlich ist.
Ausserdem kann sich der Entwickler erlauben für kleine Fixes und Refactores diese zu pushen, ohne dafür ein Issue zu erstellen,
um die Übersichtlichkeit des Issue Boards zu gewähren.