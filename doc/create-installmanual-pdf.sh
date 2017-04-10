fn=InstallationManual

pdflatex $fn.tex
bibtex $fn.aux
pdflatex $fn.tex
pdflatex $fn.tex
