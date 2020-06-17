package xadrez;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	public int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check, checkMate;
	
	private List<Peca> pecasTabuleiro = new ArrayList<>();
	private List<Peca> pecaCapturadas = new ArrayList<>();
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCA;
		iniciarConfig();
	}
	
	public int getTurno() {
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possivelMovimentos(PosicaoXadrez pInicial){
		Posicao posicao = pInicial.toPosition();
		validarPosicInicial(posicao);
		return tabuleiro.peca(posicao).possiveisMovimentos();
	}
	
	public PecaXadrez movimentoXadrez(PosicaoXadrez posicInicial, PosicaoXadrez posicFinal) {
		Posicao pInicial = posicInicial.toPosition();
		Posicao pFinal = posicFinal.toPosition();
		validarPosicInicial(pInicial);
		validarPosicFinal(pInicial, pFinal);
		Peca pecaCapturada = fazerMover(pInicial, pFinal);
		
		if (testarCheck(jogadorAtual)) {
			refazerMovimento(pInicial, pFinal, pecaCapturada);
			throw new ExcecaoXadrez("You can't put yourself in check");
		}
		check = (testarCheck(oponente(jogadorAtual))) ? true : false;
		
		if (testarCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		return (PecaXadrez)pecaCapturada;
	}
	
	private Peca fazerMover(Posicao inicial, Posicao pFinal) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(inicial);
		p.aumentarContagemMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(pFinal);
		tabuleiro.colocarPeca(p, pFinal);
		
		if (pecaCapturada != null) {
			pecasTabuleiro.remove(pecaCapturada);
			pecaCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}
	
	private void refazerMovimento(Posicao pInicial, Posicao pFinal, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(pFinal);
		p.diminuirContagemMovimento();
		tabuleiro.colocarPeca(p, pInicial);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, pFinal);
			pecaCapturadas.remove(pecaCapturada);
			pecasTabuleiro.add(pecaCapturada);
		}
	}
	
	private void validarPosicInicial(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ExcecaoXadrez("There is no piece on source position");
		}
		if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new ExcecaoXadrez("The chosen piece is not yours");
		}
		if (!tabuleiro.peca(posicao).existeMovimento()) {
			throw new ExcecaoXadrez("There is no possible moves for the chosen piece");
		}
	}
	
	private void validarPosicFinal(Posicao pInicial, Posicao pFinal) {
		if (!tabuleiro.peca(pInicial).possivelMovimento(pFinal)) {
			throw new ExcecaoXadrez("The chosen piece can't move to target position");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCA) ? Cor.PRETA : Cor.BRANCA;
	}
	
	private Cor oponente(Cor color) {
		return (color == Cor.BRANCA) ? Cor.PRETA : Cor.BRANCA;
	}
	
	private PecaXadrez Rei(Cor color) {
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == color).collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testarCheck(Cor color) {
		Posicao posicaoRei = Rei(color).getChessPosition().toPosition();
		List<Peca> pecasOponente = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(color)).collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean[][] mat = p.possiveisMovimentos();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testarCheckMate(Cor color) {
		if (!testarCheck(color)) {
			return false;
		}
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == color).collect(Collectors.toList());
		for (Peca p : lista) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i=0; i<tabuleiro.getLinhas(); i++) {
				for (int j=0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao pInicial = ((PecaXadrez)p).getChessPosition().toPosition();
						Posicao pFinal = new Posicao(i, j);
						Peca pecaCapturada = fazerMover(pInicial, pFinal);
						boolean testarCheck = testarCheck(color);
						refazerMovimento(pInicial, pFinal, pecaCapturada);
						if (!testarCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosition());
		pecasTabuleiro.add(peca);
	}
	
	private void iniciarConfig() {
		
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCA));
        
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETA));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETA));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETA));
        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETA));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETA));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETA));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETA));
	}
}
