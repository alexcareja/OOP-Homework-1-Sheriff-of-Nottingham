Student: Careja Alexandru-Cristian
Grupa: 324 CD
Tema - Sheriff of Nottingham

Pentru implementarea temei am creat urmatoarele clase:
	*	Main - Aici se desfasoara jocul, de la citirea datelor de intrare, 
	declararea jucatorilor, progresul subrundelor si al rundelor si afisarea
	scorului. La linia 60 incepe efectiv desfasurarea jocului(adica incep 
	playerii sa joace). Intr-o subrunda parcug lista de playeri, iar fiecare
	player diferit de sheriff trage 10 carti, isi creeaza sacul si il da spre
	inspectie. Dupa inspectie, cartile confiscate sunt adaugate la finalul
	pachetului. In fiecare runda sunt atatea subrunde cati jucatori sunt.
	Dupa desfasurarea jocului playerii isi vand bunurile si apoi se calculeaza
	King si Queen player pentru fiecare bun, adaugand bonusurile respective.

	*	Sack - Clasa sack are 3 caracteristici: lista de bunuri din sac, id-ul
	bunului declarat a fi in sac, si mita adusa odata cu acesta. Clasa are, pe
	langa constructor si getterii necesari, metodele addToSack si 
	removeFromSack a caror utilitate este evidenta. De asemenea am implementat
	si metoda clearSack pentru a putea crea un sac nou la fiecare subrunda

	*	Utils - Clasa are 2 metode de care am avut nevoie in implementarea 
	clasei BribedPlayer: getBestCard care returneaza cartea cu cel mai mare 
	profit din lista transmisa ca parametru si metoda neighbor care returneaza 
	true daca cei doi playeri primiti ca parametru sunt vecini

	*	BasePlayer - Are drept caracteristici de jucator un id, gold, hand
	(lista in care are cartile), un obiect de tip Sack, stall(taraba pe care 
	isi pune bunurile), score si o variabila statica s, utila in acordarea 
	cartilor din deck.
	Pe langa constructori si getteri, are 6 metode comune printre toate 
	celelalte clase derivate din BasePlayeri si 3 metode care vor fi 
	suprascrise de fiecare tip de player. Cele 6 metode comune sunt 
	urmatoarele: drawHand(care ia 10 carti in mana), updateGold(care adauga la
	banutii din buzunar suma de bani pozitiva sau negativa transmisa ca 
	parametru), addToStall(care adauga pe stall toate obiectele din lista
	transmisa ca parametru), sellItems(in care se vand bunurile de pe stall si
	se adauga bonusurile bunurilor ilegale), getCardFrequenci(care returneaza
	cate obiecte de tipul transmis ca parametru are jucatorul, useful la king 
	si queen bonus) si updateScore(care adauga la scor valoarea trimisa ca 
	parametru).
	-> createSack: Cauta cel mai frecvent bun si creeaza un sac in care pune
	bunurile cu frecventa cea mai mare, bribe 0, si bun declarat sincer. Daca
	nu are niciun bun legal, incearca sa puna cel mai scump bun ilegal daca ii
	permit banii sa plateasca un eventual penalty
	-> verify(Player): Verifica sacul playerului transmis ca parametru.
	Returneaza mita si calculeaza penalty-ul pentru bunuri nedeclarate sau
	ilegale. Daca nu exista niciun astfel de bun, atunci va plati penalty
	jucatorului pentru ca l-a inspectat pe nedrept.
	-> printScore: afiseaza id-ul, tipul player-ului("BASIC") si scorul

	* GreedyPlayer - Nu are nicio caracteristica in plus fata de jucatorul
	Base, ci doar suprascrise cele 3 metode de mai sus:
	-> createSack: apeleaza functia createSack din clasa parinte, iar in
	rundele pare adauga, daca are, cel mai scump bun ilegal in sac.
	-> verify(Player): Verifica sacul playerului transmis ca parametru. Daca
	exista mita in sac, atunci acesta il va lasa sa treaca mai departe. Altfel,
	sacul va fi inspectat fiind apelata functia verify din parinte.
	-> printScore: afiseaza id-ul, tipul player-ului("GREEDY") si scorul

	* BribedPlayer - Nu are nicio caracteristica in plus fata de jucatorul
	Base, ci doar suprascrise cele 3 metode de mai sus:
	-> createSack: Daca nu are niciun bun ilegal sau nu are macar 6 bani astfel
	incat sa nu ramana pe 0, atunci apeleaza functia createSack din parinte.
	In cazul in care are cel putin un ilegal si 6 banuti, sorteaza cartile din
	mana astfel incat sa-i fie usor sa le extraga pe cele mai profitabile si
	adauga atatea carti in sac cat ii permit banii, astfel incat sa nu ramana
	cu 0 bani daca ar plati penalty. Adauga mita 5 daca are doua sau mai putine
	ilegale in sac sau 10 daca are mai multe de doua.
	-> verify(Player): Verifica sacul playerului transmis ca parametru doar
	daca acesta este vecin cu el (verificarea se face folosind metoda neighbor
	din clasa Utils). Daca nu este vecin, ia mita de la jucator daca exista si
	lasa sacul sa treaca.
	-> printScore: afiseaza id-ul, tipul player-ului("BRIBED") si scorul