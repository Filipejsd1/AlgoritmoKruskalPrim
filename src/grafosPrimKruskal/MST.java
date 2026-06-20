import java.util.*;

public class MST {

    // Classe para representar as arestas do grafo
    static class Aresta implements Comparable<Aresta> {
        int origem, destino, peso;

        public Aresta(int origem, int destino, int peso) {
            this.origem = origem;
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public int compareTo(Aresta outra) {
            return Integer.compare(this.peso, outra.peso);
        }
    }

    // Estrutura de dados Disjoint Set (Union-Find) necessária para o Kruskal
    public static class DisjointSet {
        int[] pai, rank;

        public DisjointSet(int n) {
            pai = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                pai[i] = i;
            }
        }

        int find(int i) {
            if (pai[i] == i) return i;
            return pai[i] = find(pai[i]);
        }

        void union(int i, int j) {
            int raizI = find(i);
            int raizJ = find(j);
            if (raizI != raizJ) {
                if (rank[raizI] < rank[raizJ]) {
                    pai[raizI] = raizJ;
                } else if (rank[raizI] > rank[raizJ]) {
                    pai[raizJ] = raizI;
                } else {
                    pai[raizJ] = raizI;
                    rank[raizI]++;
                }
            }
        }
    }

    // --- ALGORITMO DE KRUSKAL ---
    public static void kruskal(List<Aresta> arestas, int numVertices) {
        Collections.sort(arestas); // Ordena as arestas por peso
        DisjointSet ds = new DisjointSet(numVertices);
        
        List<Aresta> mst = new ArrayList<>();
        int custoTotal = 0;

        for (Aresta aresta : arestas) {
            int raizOrigem = ds.find(aresta.origem);
            int raizDestino = ds.find(aresta.destino);

            // Se não formar ciclo, adiciona à MST
            if (raizOrigem != raizDestino) {
                mst.add(aresta);
                custoTotal += aresta.peso;
                ds.union(raizOrigem, raizDestino);
            }
        }

        imprimirResultado("Kruskal", mst, custoTotal);
    }

    // --- ALGORITMO DE PRIM ---
    public static void prim(List<Aresta> arestas, int numVertices) {
        // Criar lista de adjacência
        List<List<Aresta>> adj = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            adj.add(new ArrayList<>());
        }
        for (Aresta a : arestas) {
            adj.get(a.origem).add(new Aresta(a.origem, a.destino, a.peso));
            adj.get(a.destino).add(new Aresta(a.destino, a.origem, a.peso)); // Grafo não direcionado
        }

        boolean[] visitado = new boolean[numVertices];
        PriorityQueue<Aresta> pq = new PriorityQueue<>();
        List<Aresta> mst = new ArrayList<>();
        int custoTotal = 0;

        // Inicia do vértice 0 ('a')
        visitado[0] = true;
        pq.addAll(adj.get(0));

        while (!pq.isEmpty() && mst.size() < numVertices - 1) {
            Aresta arestaAtual = pq.poll();

            // Se o destino já foi visitado, ignora (evita ciclos)
            if (visitado[arestaAtual.destino]) continue;

            // Adiciona na MST
            visitado[arestaAtual.destino] = true;
            custoTotal += arestaAtual.peso;
            mst.add(arestaAtual);

            // Adiciona as novas arestas adjacentes à Fila de Prioridade
            for (Aresta proxima : adj.get(arestaAtual.destino)) {
                if (!visitado[proxima.destino]) {
                    pq.add(proxima);
                }
            }
        }

        imprimirResultado("Prim", mst, custoTotal);
    }

    // Método auxiliar para formatar e imprimir o resultado
    private static void imprimirResultado(String algoritmo, List<Aresta> mst, int custoTotal) {
        System.out.println("--- Algoritmo de " + algoritmo + " ---");
        System.out.println("Arestas na Árvore Geradora Mínima:");
        for (Aresta a : mst) {
            char u = (char) ('a' + a.origem);
            char v = (char) ('a' + a.destino);
            System.out.println("(" + u + " - " + v + ") : Peso " + a.peso);
        }
        System.out.println("Custo Total da MST: " + custoTotal + "\n");
    }

    // --- MÉTODO PRINCIPAL (MAIN) ---
    public static void main(String[] args) {
        // Mapeamento: a=0, b=1, c=2, d=3, e=4, f=5, g=6, h=7, i=8, j=9, k=10, l=11

        System.out.println("=====================================");
        System.out.println("              FIGURA 1               ");
        System.out.println("=====================================");
        int numVerticesFig1 = 8; // a até h
        List<Aresta> fig1 = Arrays.asList(
            new Aresta(0, 1, 2), // a-b
            new Aresta(0, 2, 3), // a-c
            new Aresta(1, 3, 2), // b-d
            new Aresta(2, 3, 1), // c-d
            new Aresta(3, 4, 2), // d-e
            new Aresta(3, 5, 4), // d-f
            new Aresta(4, 5, 1), // e-f
            new Aresta(4, 6, 2), // e-g
            new Aresta(5, 6, 2), // f-g
            new Aresta(5, 7, 1), // f-h
            new Aresta(6, 7, 3)  // g-h
        );

        kruskal(fig1, numVerticesFig1);
        prim(fig1, numVerticesFig1);

        System.out.println("=====================================");
        System.out.println("              FIGURA 2               ");
        System.out.println("=====================================");
        int numVerticesFig2 = 12; // a até l
        List<Aresta> fig2 = Arrays.asList(
            new Aresta(0, 1, 16), // a-b
            new Aresta(0, 2, 10), // a-c
            new Aresta(0, 9, 12), // a-j
            new Aresta(1, 2, 7),  // b-c
            new Aresta(1, 4, 2),  // b-e
            new Aresta(1, 3, 13), // b-d
            new Aresta(2, 4, 1),  // c-e
            new Aresta(2, 6, 21), // c-g
            new Aresta(3, 6, 15), // d-g
            new Aresta(4, 5, 9),  // e-f
            new Aresta(4, 10, 4), // e-k
            new Aresta(5, 10, 8), // f-k
            new Aresta(5, 6, 3),  // f-g
            new Aresta(5, 7, 20), // f-h
            new Aresta(6, 7, 18), // g-h
            new Aresta(6, 8, 17), // g-i
            new Aresta(7, 9, 19), // h-j
            new Aresta(8, 9, 5),  // i-j
            new Aresta(8, 10, 6), // i-k
            new Aresta(8, 11, 14),// i-l
            new Aresta(10, 11, 11)// k-l
        );

        kruskal(fig2, numVerticesFig2);
        prim(fig2, numVerticesFig2);
    }
}