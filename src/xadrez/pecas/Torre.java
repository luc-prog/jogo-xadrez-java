package xadrez.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez{

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		
	}
	
	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		//cima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		// direita
		p.setValues(posicao.getLinha(), posicao.getColuna() +1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() +1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//cima
				p.setValues(posicao.getLinha() + 1, posicao.getColuna());
				while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setLinha(p.getLinha() + 1);
				}
				if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
		return mat;
	}
}
