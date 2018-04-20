FROM buildpack-deps:stretch as builder
RUN mkdir /tmp/dapg \
    && wget -q http://www.inf.udec.cl/~chernand/sources/dapg/dapg_prot_complexes-v1.1.tgz -O /tmp/dapg/dapg_prot_complexes.tgz \
    && cd /tmp/dapg/ && tar xvf dapg_prot_complexes.tgz \
    && cd dapg_complexes && make

FROM python:3.6-stretch

# Some ideas from https://github.com/in-silico/judge-bot/blob/768238c1eb0875a1d7800343755d70fd432a28c2/debian/Dockerfile
# Install R, libhdf5 and nodejs
RUN echo "deb http://ftp.de.debian.org/debian testing contrib" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing non-free" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing main" >> /etc/apt/sources.list \
    && echo "deb http://downloads.skewed.de/apt/stretch stretch main" >> /etc/apt/sources.list \
    && echo "deb-src http://downloads.skewed.de/apt/stretch stretch main" >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y --force-yes git \
                                      libhdf5-dev \
                                      nodejs \
                                      openjdk-8-jdk \
                                      python3-graph-tool \
                                      r-base \
    && apt-get -yq autoremove \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install some needed R packages
RUN Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); source("https://bioconductor.org/biocLite.R"); biocLite("minet")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); source("http://bioconductor.org/biocLite.R"); biocLite(c("AnnotationDbi", "impute", "GO.db", "preprocessCore", "ReactomePA", "clusterProfiler", "meshes", "org.Hs.eg.db", "GOSemSim")); install.packages("WGCNA")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("precrec")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("ggplot2")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("ggrepel")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("corpcor")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("fdrtool")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("ppcor")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("matrixcalc")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=1); install.packages("devtools"); library("devtools"); install_github("wyguo/RLowPC", ref="a3f741a")' \
    # Cache Reactome and GO Data so it's faster when we need to run it
    && Rscript -e 'library(clusterProfiler); library(org.Hs.eg.db); library(ReactomePA); enrichGO(c(4312, 8318, 10874, 55143, 55388, 991), OrgDb=org.Hs.eg.db, ont="MF"); enrichPathway(c(4312, 8318, 10874, 55143, 55388, 991));'

COPY --from=builder /tmp/dapg/dapg_complexes/bin/* /usr/bin/

ADD requirements.txt /requirements.txt
RUN pip install --upgrade pip \
    && pip install --upgrade -r /requirements.txt \
    && rm /requirements.txt \
    && rm -rf ~/.cache/pip \
    && rm -rf /src

RUN mkdir -p /root/.config/matplotlib && echo 'backend : Agg' >> /root/.config/matplotlib/matplotlibrc
