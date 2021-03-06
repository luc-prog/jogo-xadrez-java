package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	public int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check, checkMate;
	private PecaXadrez enPassantVulnerable, promoted;

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
	
	public PecaXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public PecaXadrez getPromoted() {
		return promoted;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possivelMovimentos(PosicaoXadrez pInicial) {
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
		
		PecaXadrez movedPiece = (PecaXadrez)tabuleiro.peca(pFinal);
		
		// #specialmove promotion
		promoted = null;
		if (movedPiece instanceof Peao) {
			if ((movedPiece.getCor() == Cor.BRANCA && pFinal.getLinha() == 0)
					|| (movedPiece.getCor() == Cor.PRETA && pFinal.getLinha() == 7)) {
				promoted = (PecaXadrez) tabuleiro.peca(pFinal);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testarCheck(oponente(jogadorAtual))) ? true : false;

		if (testarCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}
		
		// #specialmove en passant
		if (movedPiece instanceof Peao && (pFinal.getLinha() == pInicial.getLinha() - 2 || pFinal.getLinha() == pInicial.getLinha() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}

		return (PecaXadrez) pecaCapturada;
	}
	
	public PecaXadrez replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			return promoted;
		}

		Posicao pos = promoted.getChessPosition().toPosition();
		Peca p = tabuleiro.removerPeca(pos);
		pecasTabuleiro.remove(p);

		PecaXadrez newPiece = newPiece(type, promoted.getCor());
		tabuleiro.colocarPeca(newPiece, pos);
		pecasTabuleiro.add(newPiece);

		return newPiece;
	}

	private PecaXadrez newPiece(String type, Cor color) {
		if (type.equals("B")) return new Bispo(tabuleiro, color);
		if (type.equals("N")) return new Cavalo(tabuleiro, color);
		if (type.equals("Q")) return new Rainha(tabuleiro, color);
		return new Torre(tabuleiro, color);
	}
	
	private Peca fazerMover(Posicao inicial, Posicao pFinal) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(inicial);
		p.aumentarContagemMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(pFinal);
		tabuleiro.colocarPeca(p, pFinal);

		if (pecaCapturada != null) {
			pecasTabuleiro.remove(pecaCapturada);
			pecaCapturadas.add(pecaCapturada);
		}
		
		// #specialmove castling kingside rook
		if (p instanceof Rei && pFinal.getColuna() == inicial.getColuna() + 2) {
			Posicao inicialT = new Posicao(inicial.getLinha(), inicial.getColuna() + 3);
			Posicao pFinalT = new Posicao(inicial.getLinha(), inicial.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(inicialT);
			tabuleiro.colocarPeca(torre, pFinalT);
			torre.diminuirContagemMovimento();
		}
		
		// #specialmove castling kingside rook
		if (p instanceof Rei && pFinal.getColuna() == inicial.getColuna() - 2) {
			Posicao inicialT = new Posicao(inicial.getLinha(), inicial.getColuna() - 4);
			Posicao pFinalT = new Posicao(inicial.getLinha(), inicial.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(inicialT);
			tabuleiro.colocarPeca(torre, pFinalT);
			torre.diminuirContagemMovimento();
		}
		// #specialmove en passant
		if (p instanceof Peao) {
			if (inicial.getColuna() != pFinal.getColuna() && pecaCapturada == null) {
				Posicao pawnPosition;
				if (p.getCor() == Cor.BRANCA) {
					pawnPosition = new Posicao(pFinal.getLinha() + 1, pFinal.getColuna());
				} else {
					pawnPosition = new Posicao(pFinal.getLinha() - 1, pFinal.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(pawnPosition);
				pecaCapturadas.add(pecaCapturada);
				pecasTabuleiro.remove(pecaCapturada);
			}
		}
		return pecaCapturada;
	}

	private void refazerMovimento(Posicao inicial, Posicao pFinal, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(pFinal);
		p.diminuirContagemMovimento();
		tabuleiro.colocarPeca(p, inicial);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, pFinal);
			pecaCapturadas.remove(pecaCapturada);
			pecasTabuleiro.add(pecaCapturada);
		}
		
		// #specialmove castling kingside rook
		if (p instanceof Rei && pFinal.getColuna() == inicial.getColuna() + 2) {
			Posicao inicialT = new Posicao(inicial.getLinha(), inicial.getColuna() + 3);
			Posicao pFinalT = new Posicao(inicial.getLinha(), inicial.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(pFinalT);
			tabuleiro.colocarPeca(torre, inicialT);
			torre.diminuirContagemMovimento();
		}

		// #specialmove castling kingside rook
		if (p instanceof Rei && pFinal.getColuna() == inicial.getColuna() - 2) {
			Posicao inicialT = new Posicao(inicial.getLinha(), pFinal.getColuna() - 4);
			Posicao pFinalT = new Posicao(inicial.getLinha(), pFinal.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(pFinalT);
			tabuleiro.colocarPeca(torre, inicialT);
			torre.diminuirContagemMovimento();
		}
		
		// #specialmove en passant
		if (p instanceof Peao) {
			if (inicial.getColuna() != pFinal.getColuna() && pecaCapturada == enPassantVulnerable) {
				PecaXadrez pawn = (PecaXadrez) tabuleiro.removerPeca(pFinal);
				Posicao pawnPosition;
				if (p.getCor() == Cor.BRANCA) {
					pawnPosition = new Posicao(3, pFinal.getColuna());
				} else {
					pawnPosition = new Posicao(4, pFinal.getColuna());
				}
				tabuleiro.colocarPeca(pawn, pawnPosition);
			}
		}
	}
	

	private void validarPosicInicial(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ExcecaoXadrez("There is no piece on source position");
		}
		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
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
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == color)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	private boolean testarCheck(Cor color) {
		Posicao posicaoRei = Rei(color).getChessPosition().toPosition();
		List<Peca> pecasOponente = pecasTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(color))
				.collect(Collectors.toList());
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
		List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == color)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao pInicial = ((PecaXadrez) p).getChessPosition().toPosition();
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

		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCA));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCA, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCA, this));

		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETA));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETA));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETA));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETA));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETA));
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETA, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETA, this));
	}
}
