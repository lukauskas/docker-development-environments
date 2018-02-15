FROM python:3.6

# Some ideas from https://github.com/in-silico/judge-bot/blob/768238c1eb0875a1d7800343755d70fd432a28c2/debian/Dockerfile
# Install R, libhdf5 and nodejs
RUN echo "deb http://ftp.de.debian.org/debian testing contrib" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing non-free" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing main" >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y libhdf5-dev \
                          nodejs \
                          r-base \
    && apt-get -yq autoremove \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install some needed R packages
RUN Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); source("https://bioconductor.org/biocLite.R"); biocLite("minet")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); source("http://bioconductor.org/biocLite.R"); biocLite(c("AnnotationDbi", "impute", "GO.db", "preprocessCore")); install.packages("WGCNA")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("precrec")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("ggplot2")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("ggrepel")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("corpcor")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("fdrtool")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("ppcor")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("matrixcalc")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("devtools"); library("devtools"); install_github("wyguo/RLowPC", ref="a3f741a")'

ADD requirements.txt /requirements.txt
RUN pip install --upgrade pip \
    && pip install --upgrade -r /requirements.txt \
    && rm /requirements.txt \
    && rm -rf ~/.cache/pip \
    && rm -rf /src

RUN mkdir -p /root/.config/matplotlib && echo 'backend : Agg' >> /root/.config/matplotlib/matplotlibrc
