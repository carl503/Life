# Äquivalenzklassen

| #    | Typ      | Beschreibung            |
| ---- | -------- | ----------------------- |
| 1    | Gültig   | Gültiges GameObject     |
| 2    | Gültig   | Gültige Position        |
| 3    | Gültig   | Gültige LifeForm        |
| 4    | Gültig   | Gültiges AnimalObject   |
| 5    | Ungültig | Null Argument           |
| 6    | Ungültig | Negative Anzahl Spalten |
| 7    | Ungültig | Negative Anzahl Reihen  |



# TestCases
## Board

| Methode          | testAddGameObject                                            |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 1, 2                                                         |
| Input            | GameObject  1: Position [x:0, y:0]<br />GameObject  2: Position [x:0, y:1] |
| Output           | Anzahl GameObjects: 2<br />Anzahl Positionen: 2              |
| Status           | Passed                                                       |

| Methode          | testCleanBoard                                               |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 3                                                            |
| Input            | LifeForm 1: Position [x:0, y:0] / Ist tot: true<br />LifeForm 2: Position [x:0, y:1] / Ist tot: false<br />LifeForm 3: Position [x:0, y:1] / Ist tot: false |
| Output           | Anzahl GameObjects: 2<br />Anzahl Positionen: 1              |
| Status           | Passed                                                       |

| Methode          | testContainsNotInstanceOfAnimalObject                        |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 4                                                            |
| Input            | AnimalObject 1: Typ MeatEater / Position [x:0, y:1]<br />AnimalObject 2: Typ AnimalObject |
| Output           | Typ MeatEater: true<br />Typ AnimalObject: false             |
| Status           | Passed                                                       |

| Methode          | testAddGameObjectNull                                        |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 5                                                            |
| Input            | Null                                                         |
| Output           | NullPointerException<br />Message: "Game object cannot be null to add it on the board." |
| Status           | Passed                                                       |

| Methode          | testContainsNotInstanceOfAnimalObjectNull              |
| ---------------- | ------------------------------------------------------ |
| Äquivalenzklasse | 5                                                      |
| Input            | BoardSet: 1x MeatEater / Position [x:0, y:0]<br />null |
| Output           | false                                                  |
| Status           | Passed                                                 |

| Methode          | testInvalidConstructorRowLowerThanMinValue                   |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 6                                                            |
| Input            | Number of Rows: MIN_ROWS (3) - 1<br />Number of Columns: MIN_COLUMNS (3) |
| Output           | IllegalArgumentException<br />Message: "The number of rows cannot be less than " + MIN_ROWS |
| Status           | Passed                                                       |

| Methode          | testInvalidConstructorColumnsLowerThanMinValue               |
| ---------------- | ------------------------------------------------------------ |
| Äquivalenzklasse | 7                                                            |
| Input            | Number of Rows: MIN_ROWS (3)<br />Number of Columns: MIN_COLUMNS (3) - 1 |
| Output           | IllegalArgumentException<br />Message: "The number of columns cannot be less than " + MIN_COLUMNS |
| Status           | Passed                                                       |

| Methode          |      |
| ---------------- | ---- |
| Äquivalenzklasse |      |
| Input            |      |
| Output           |      |
| Status           |      |
