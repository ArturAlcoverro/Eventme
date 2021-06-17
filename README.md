

<h1 style="color:#6D39DC; font-weight:bold">Eventme</h1>

### **ARTUR ALCOVERRO**

### **EDMON BOSCH**

### **DAVID MARQUET**

### **JOAN CASALS**

 <br/><br/>
## **Desenvolupament en dispositius mòbils (Android)**

https://user-images.githubusercontent.com/29460917/122480373-0a1f4980-cfcd-11eb-8c76-8c24e8c5d8f5.mp4

 <br/><br/>

**Introducció:**

Des de la salle s’ha encarregat als alumnes a implementar un projecte completament transversal, per aconseguir una mateixa plataforma que doni servei tant en Web com en aplicació de mòbil Android, a part de la seva corresponent API on obtenir i guardar tota la informació respecte als usuaris i les seves accions i preferències. En el cas de l’assignatura de desenvolupament mòbils l’objectiu és elaborar l’aplicació Eventme per dispositius Android exclusivament.

Eventme és una xarxa social principalment d’esdeveniments on cadascú és benvingut a formar part d’ella. Tens l'oportunitat d'assistir als teus esdeveniments preferits sobre diferents temàtiques (música, cultura, art, moda, esports, videojocs…), preguntar i sol·licitar qualsevol informació als creadors de l’event. A part tu mateix pots organitzar els teus propis events per conèixer nova gent i més endavant tenir nous amics amb els qui mantenir converses dins de la plataforma.

L’aplicació completa està realitzada amb la IDE Android Studio, el projecte està dissenyat per combinar dos patrons de disseny diferents Model-View-Controller + Layered Architecture, més endavant en el desenvolupament entrarem més a fons en el perquè la seva utilització i els seus avantatges. Abans de posar-nos en la implementació del codi donem una explicació del disseny marcat prèviament amb els wireframes i mockups que han inspirat l’aplicació i una idea general abans de començar a picar codi.

Els objectius del Projecte són diferents, en primer lloc trobem la capacitat de treballar en grup, comunicar-nos entre si i organitzar de forma responsable i equitativa les diferents tasques a dur a terme. Llavors la consolidació de tots els aspectes teòrics i pràctics realitzats durant l’assignatura que hem de plasmar en la implementació per demostrar que s’han assolit els coneixements necessaris en el desenvolupament d’aplicacions mòbils. Finalment l’objectiu personal de cadascun de nosaltres està en el correcte desenvolupament per obtenir un producte final decent on l’usuari se senti còmode amb l’aplicació i respondre a les seves necessitats proporcionant-li una plataforma social sobre esdeveniments.

 <br/><br/>


**Desenvolupament:**

El projecte està basat en dos patrons de disseny diferents que junts ofereixen molt bona coherència i permeten l'abstracció dels diferents mòduls i repercuteix positivament a l’hora de repartir la feina. És el cas del Model-View-Controller + Layered Architecture. El projecte consisteix en 4 parts, per separat tenim tots els layouts xml que configuren les vistes del programa recalcan al màxim els mockups. Llavors passarem més a la part de codi en sí, on tenim la carpeta Bussines, en aquesta es troben les diferents entities del programa podríem dir-ne el model que defineixen l'estructura de la informació i el tipus que rebem quan més endavant fem les peticions a la API per obtenir tota la informació. És per això que llavors tenim el mòdul de Persistencia on es troben les classes i interfícies encarregades d'utilitzar Retrofit per fer les crides a la API demanant la informació que el programa necessita o bé enviar informació a aquesta per incorporar nous registres i accions de la plataforma. Finalment dins la carpeta presentació trobem els que seran els controladors de les vistes que incorporaran certa lògica també, entre ells hi ha la carpeta activitats, fragments i adapters. Els fragments consisteixen en 4 que son les pantalles principals de l’aplicació com es la Home, Creació d’Event, Xat i Perfil. Llavors els adapters son classes utilitzades per processar llistes i mostrar-les a una vista quan tenim un recyclerview normalment, cada un d’aquests té el seu Adapter personal que carrega el layout exclusiu per cada item.

L’ordre que hem seguit en el desenvolupament de tot el parlat anteriorment ha sigut el següent, primer de tot un cop vam tenir el disseny dels mockups finalitzats vàrem procedir a realitzar tots els layouts complets inclós els del items de les llistes. Llavors vam crear el model de classes que necessita l’aplicació per recollir la informació de la API, conjuntament amb desenvolupar tots els adapter ja que a la fi la majoria son idèntics canviant el tipus de llista que reben i el layout que han de carregar. Llavors de forma paralela ens vam posar a implementar les crides a la API a mesura que es necessitaven mentres un altre company feia les activitats o fragment per controlar la interacció de l’usuari amb la vista i segons les seves accions requerir o enviar informació a el endpoint corresponent i donar resposta sobre l'acció en la interfície gràfica.

Finalment comentar que hem afegit funcionalitats opcionals no requerides en l’enunciat com el multi idioma en que detectem l’idioma del dispositiu perquè l’aplicació tingui el mateix, poder fer logout de la sessió d’usuari, poder decidir entre mode diürn i mode nocturn, visualitzar en el detall de l’event el mapa amb la seva localització i també l’enviament de imatges a través del xat.

 <br/><br/>

**Resolució de Problemes:**

Durant el desenvolupament del projecte si que ens hem anat trobant diferents errors on el codi no compilava, molts d’ells s’han solucionat investigant a Internet perquè és degut l’error i quines possibles solucions ofereix la comunitat o bé quin és el motiu pel qual succeeix i llavors poder esbrinar on tenim l’error en el nostre codi. En el cas d’afegir certs aspectes opcionals que no entraven en la teoria de la classe per poder resoldre les necessitats ens hem guiat amb la documentació d’Android Developers i recalcar que hem seguit els patrons de disseny de Materials Design amb els Textfields, ús de BottomSheets per fer filtres, DateTimePickers, Buttons, etc.


Finalment, esmentar també que hem seguit una metodología en que quan un company es trobava estancat en una funcionalitat delegar aquesta a un altre company perquè a vegades és important que uns diferents ulls mirant el problema i puguin trobar possibles solucions de forma conjunta.

 <br/><br/>

**Conclusions:**

Les conclusions extretes del projecte en son moltes, però a continuació ens agradaria citar les que considerem més importants i les quals ens han aportat un valor més gran. Creiem que hem assolit tots els objectius marcats en un principi i assolit els coneixements. Sobretot aprofundir en el sentit que hem anat més enllà de les eines apreses a classe i implementat llibreries externes que ens permetin fer diferents funcionalitats i afegir funcionalitats extres no especificades en l'enunciat per donar un toc propi i personal a l’aplicació i que l'experiència de l’usuari es vegi incrementada el màxim.

A més a més, la magnitud del projecte implica haver de aplicar tots els conceptes apresos durant la teoria i pràctica de l'assignatura seguir millorant també la base apresa en altres assignatures anteriors en un projecte més real i llarg per enfrontar-te a problemes que et permeten millorar com a programador. I sobretot consolidar la teoria, ja que en el fons on realment s'aprèn és practicant i equivocant-se.

Finalment dir que estem contents en global del resultat del projecte, de com s’ha organitzat el projecte, de com hem après els conceptes, de saber treballar en grup i ajudar-nos mútuament, una bona estructura i implementació del codi, a més a més d’aconseguir recalcar al màxim les vistes perquè siguin el més similar possible als mockups presentats anteriorment.

