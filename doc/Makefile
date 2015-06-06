LATEX = pdflatex

DOCS = overview

all: ${DOCS}

overview:
	$(LATEX) manual.tex
	$(LATEX) manual.tex

clean:
	rm -f *.pdf *.log *.aux
