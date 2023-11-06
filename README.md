# Règles

On joue au poker Texas Hold'em. Toutes les règles du poker sont reprises. Il y a 4 joueurs qui jouent à une table. Ils commencent tous avec 300 jetons. Le gagnant est
le dernier à quitter la table. Un joueur quitte la table lorsqu'il n'a plus de jetons. Pour jouer une manche, il faut avoir misé autant que tous les autres joueurs, ou avoir fait tapis (i.e. misé tous ses jetons).
Il y a une couleur d'atout, décidée par la grande blinde, qui inverse l'ordre de puissance des cartes pendant une manche. Chaque joueur peut échanger, quand il veut, 50 jetons contre une carte supplémentaire, qu'il doit montrer à tout le monde. Il peut choisir de payer 100 jetons pour garder la carte secrète. Il peut le faire 1 fois par manche. Chaque joueur peut payer 100 jetons pour voir une des cartes de son adversaire, et 200 jetons pour détruire la carte.
## Mise en place

Le maître du jeu est le croupier. Le croupier distribue les cartes aux joueurs (2 cartes par joueur) et distribue les cartes au milieu (3,puis 1, puis 1). Il demande également les mises aux joueurs et distribue les gains en fin de manche.

## Déroulé de la partie

Le croupier distribue les cartes aux joueurs, puis prend les mises automatiques ("Blindes"). Les joueurs prennent connaissance de leurs cartes, puis ont 3 choix : call (miser autant que la plus haute mise), fold (se coucher, sortir de la manche et perdre toute sa mise actuelle), et raise (miser plus que la plus haute mise). Un tour de mise s'arrête lorsque tous les joueurs ont call, ou qu'il ne reste plus qu'un joueur pouvant miser, car tous les autres sont soit tapis, soit couchés. Il y a 4 tours de mise en tout : avant le turn (3 cartes au milieu), après le turn, après le flop (4eme carte au milieu) et après le river (5e carte au milieu). Après les 4 tours de mise, les joueurs toujours en jeu dans la manche montrent leur main, et le croupier décide qui a gagné et combien suivant les règles classiques du Texas Hold'Em. Il y a un système de pots pour gérer les gains des joueurs all-in.


## Fin de la partie

La partie est terminée lorsqu'il ne reste plus qu'un joueur à la table. Jusque là, les manches s'enchaînent. Les blindes nous assurent une mise minimum à chaque manche, donc que la partie termine.

# Détail des classes principales
TODO

   * LocalTexasHoldEm la version du jeu supportant le jeu en local
   * TexasHoldEmEngine le moteur du jeu
   * TexasHoldEmPlayer le joueur distant en cas de partie réseau
   * TexasHoldEmNetWworkEngine la version du jeu supportant le réseau

# Protocole réseau

TODO

> Le protocole réseau définit les séquences des commandes échangées entre les différentes parties prenantes. Il doit contenir, pour chaque commande, l'expéditeur, le destinataire, le nom de la commande et le contenu du corps de la commande.

![protocole Poker](protocole.png)


